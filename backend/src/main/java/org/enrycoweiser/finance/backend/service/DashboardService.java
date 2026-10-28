package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.*;
import org.enrycoweiser.finance.backend.repository.*;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.DashboardRequest;
import org.enrycoweiser.finance.shared.api.response.DashboardResponse;
import org.enrycoweiser.finance.shared.dto.CategoryAmountDto;
import org.enrycoweiser.finance.shared.dto.DashboardDto;
import org.enrycoweiser.finance.shared.dto.LiquidityDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransferRepository transferRepository;
    private final InvestmentHistoryRepository investmentHistoryRepository;

    /*
        Summary of a single month:
        money in  = incomes of the month
        money out = expenses of the month + monthly amount of the subscriptions covering it
     */
    @Transactional
    public DashboardResponse getSummary(DashboardRequest request) {
        DashboardResponse dashboardResponse = new DashboardResponse();

        if(request == null || request.getYear() == null || request.getMonth() == null) {
            dashboardResponse.createErrorResponse(ErrorUtils.API_008, ErrorUtils.API_008_CODE);
            return dashboardResponse;
        }

        try {
            LocalDate dateFrom = LocalDate.of(request.getYear(), request.getMonth(), 1);
            LocalDate dateTo = dateFrom.withDayOfMonth(dateFrom.lengthOfMonth());

            Map<String, BigDecimal> incomes = sumIncomes(dateFrom, dateTo);
            Map<String, BigDecimal> expenses = sumExpenses(dateFrom, dateTo);

            DashboardDto dto = new DashboardDto();
            dto.setYear(request.getYear());
            dto.setMonth(request.getMonth());
            dto.setMoneyIn(total(incomes));
            dto.setMoneyOut(total(expenses));
            dto.setIncomeCategories(toCategoryAmounts(incomes));
            dto.setExpenseCategories(toCategoryAmounts(expenses));
            dto.setLiquidity(calculateLiquidity(LocalDate.now()));

            dashboardResponse = new DashboardResponse(List.of(dto));
        } catch (Exception ex) {
            dashboardResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return dashboardResponse;
        }

        dashboardResponse.createResponse();

        return dashboardResponse;
    }

    protected Map<String, BigDecimal> sumIncomes(LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.INCOME_DATE_FROM, dateFrom);
        filters.put(FilterUtils.INCOME_DATE_TO, dateTo);

        Map<String, BigDecimal> sums = new HashMap<>();
        for (Income i : incomeRepository.retrieveByFilters(filters)) {
            String category = i.getCategory() != null ? i.getCategory() : StaticUtils.INCOME_OTHER;
            add(sums, category, i.getMoney());
        }

        return sums;
    }

    protected Map<String, BigDecimal> sumExpenses(LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.EXPENSE_DATE_FROM, dateFrom);
        filters.put(FilterUtils.EXPENSE_DATE_TO, dateTo);

        Map<String, BigDecimal> sums = new HashMap<>();
        for (Expense e : expenseRepository.retrieveByFilters(filters)) {
            String category = e.getCategory() != null ? e.getCategory() : StaticUtils.EXPENSE_OTHER;
            add(sums, category, e.getMoney());
        }

        Map<String, Object> subscriptionFilters = new HashMap<>();
        subscriptionFilters.put(FilterUtils.SUBSCRIPTION_HISTORY_DATE_FROM, dateFrom);
        subscriptionFilters.put(FilterUtils.SUBSCRIPTION_HISTORY_DATE_TO, dateTo);

        for (SubscriptionHistory h : subscriptionHistoryRepository.retrieveByFilters(subscriptionFilters)) {
            add(sums, StaticUtils.SUBSCRIPTION_CATEGORY, h.monthlyAmount());
        }

        return sums;
    }

    /*
        Balance of every payment method, from the first movement up to the given date:
          + incomes                     - expenses
          + incoming transfers          - outgoing transfers
          - subscriptions (full price on the start date)
          - buys (amount + fee)         + sells (amount - fee)
        A movement with an empty payment method (e.g. an opening balance transfer with no "from")
        does not belong to any payment method and is skipped on that side.
        Only active payment methods are shown, plus inactive ones that still have money on them.
     */
    protected List<LiquidityDto> calculateLiquidity(LocalDate date) {
        Map<Long, BigDecimal> balances = new HashMap<>();

        Map<String, Object> incomeFilters = new HashMap<>();
        incomeFilters.put(FilterUtils.INCOME_DATE_TO, date);
        for (Income i : incomeRepository.retrieveByFilters(incomeFilters)) {
            move(balances, i.getPaymentMethod(), i.getMoney());
        }

        Map<String, Object> expenseFilters = new HashMap<>();
        expenseFilters.put(FilterUtils.EXPENSE_DATE_TO, date);
        for (Expense e : expenseRepository.retrieveByFilters(expenseFilters)) {
            move(balances, e.getPaymentMethod(), negate(e.getMoney()));
        }

        Map<String, Object> transferFilters = new HashMap<>();
        transferFilters.put(FilterUtils.TRANSFER_DATE_TO, date);
        for (Transfer t : transferRepository.retrieveByFilters(transferFilters)) {
            move(balances, t.getFromPaymentMethod(), negate(t.getMoney()));
            move(balances, t.getToPaymentMethod(), t.getMoney());
        }

        // without DATE_FROM, DATE_TO selects the periods started up to the date
        Map<String, Object> subscriptionFilters = new HashMap<>();
        subscriptionFilters.put(FilterUtils.SUBSCRIPTION_HISTORY_DATE_TO, date);
        for (SubscriptionHistory h : subscriptionHistoryRepository.retrieveByFilters(subscriptionFilters)) {
            move(balances, h.getPaymentMethod(), negate(h.getPrice()));
        }

        Map<String, Object> investmentFilters = new HashMap<>();
        investmentFilters.put(FilterUtils.INVESTMENT_HISTORY_DATE_TO, date);
        for (InvestmentHistory h : investmentHistoryRepository.retrieveByFilters(investmentFilters)) {
            if(h.getAmount() == null) {
                continue;
            }

            BigDecimal fee = h.getFee() != null ? h.getFee() : BigDecimal.ZERO;
            if(h.isBuy()) {
                move(balances, h.getPaymentMethod(), negate(h.getAmount().add(fee)));
            } else if(h.isSell()) {
                move(balances, h.getPaymentMethod(), h.getAmount().subtract(fee));
            }
        }

        List<LiquidityDto> dtos = new ArrayList<>();
        for (PaymentMethod pm : paymentMethodRepository.retrieveByUser(SEManager.getId())) {
            BigDecimal balance = balances.getOrDefault(pm.getId(), BigDecimal.ZERO);

            if(!Boolean.TRUE.equals(pm.getActive()) && balance.signum() == 0) {
                continue;
            }

            LiquidityDto dto = new LiquidityDto();
            dto.setPaymentMethod(pm.getName());
            dto.setBalance(balance);
            dtos.add(dto);
        }

        dtos.sort(Comparator.comparing(LiquidityDto::getPaymentMethod, Comparator.nullsLast(String::compareToIgnoreCase)));

        return dtos;
    }

    protected void move(Map<Long, BigDecimal> balances, PaymentMethod paymentMethod, BigDecimal amount) {
        if(paymentMethod == null || paymentMethod.getId() == null || amount == null) {
            return;
        }

        balances.merge(paymentMethod.getId(), amount, BigDecimal::add);
    }

    protected BigDecimal negate(BigDecimal amount) {
        return amount != null ? amount.negate() : null;
    }

    protected void add(Map<String, BigDecimal> sums, String category, BigDecimal amount) {
        if(amount == null) {
            return;
        }

        sums.merge(category, amount, BigDecimal::add);
    }

    protected BigDecimal total(Map<String, BigDecimal> sums) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal amount : sums.values()) {
            total = total.add(amount);
        }

        return total;
    }

    /*
        Categories ordered from the biggest to the smallest, with their percentage of the total.
     */
    protected List<CategoryAmountDto> toCategoryAmounts(Map<String, BigDecimal> sums) {
        BigDecimal total = total(sums);

        List<CategoryAmountDto> dtos = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> sum : sums.entrySet()) {
            CategoryAmountDto dto = new CategoryAmountDto();
            dto.setCategory(sum.getKey());
            dto.setAmount(sum.getValue());
            if(total.signum() == 0) {
                dto.setPercentage(BigDecimal.ZERO);
            } else {
                dto.setPercentage(sum.getValue()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(total, 1, RoundingMode.HALF_UP));
            }
            dtos.add(dto);
        }

        dtos.sort(Comparator.comparing(CategoryAmountDto::getAmount).reversed());

        return dtos;
    }
}
