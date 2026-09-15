package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Expense;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.ExpenseRepository;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.ExpenseFilter;
import org.enrycoweiser.finance.shared.api.request.ExpenseRequest;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.dto.ExpenseDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseService implements EntityService<ExpenseDto,
                                                        ExpenseRequest,
                                                        ExpenseResponse> {

    private final ExpenseRepository expenseRepository;

    @Transactional
    @Override
    public ExpenseResponse save(ExpenseRequest request) {
        Expense e;
        ExpenseResponse expenseResponse = new ExpenseResponse();
        Optional<Expense> expense;

        try {
            expense = expenseRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            expenseResponse.createErrorResponse("", ex.getMessage());
            return expenseResponse;
        }

        if(expense.isPresent()) {
            e = expense.get();
        } else {
            e = new Expense();
            e.setDate(request.getDate());
            e.setMoney(request.getMoney());
            e.setLocation(request.getLocation());
            e.setStore(request.getStore());
            e.setCategory(request.getCategory());
            e.setPaymentMethod(new PaymentMethod(request.getPaymentMethod()));
            e.setNote(request.getNote());
            e.setUserId(request.getUserId());
        }

        try {
            expenseRepository.save(e);
        } catch (Exception ex) {
            expenseResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return expenseResponse;
        }

        expenseResponse.createResponse();

        return expenseResponse;
    }

    @Transactional
    @Override
    public ExpenseResponse delete(ExpenseRequest request) {
        ExpenseResponse expenseResponse = new ExpenseResponse();
        Optional<Expense> expense;

        try {
            expense = expenseRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            expenseResponse.createErrorResponse("", ex.getMessage());
            return expenseResponse;
        }

        if(expense.isPresent()) {
            try {
                expenseRepository.delete(expense.get());
            } catch (Exception ex) {
                expenseResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return expenseResponse;
            }
        } else {
            expenseResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return expenseResponse;
        }

        expenseResponse.createResponse();

        return expenseResponse;
    }

    @Transactional
    @Override
    public ExpenseResponse retrieve(ExpenseRequest request) {
        ExpenseResponse expenseResponse = new ExpenseResponse();
        Map<String, Object> filters = ExpenseFilter.createFilterMap(request);

        try {
            List<Expense> expenses = expenseRepository.retrieveByFilters(filters);

            List<ExpenseDto> dtos = new ArrayList<>();
            for (Expense e : expenses) {
                ExpenseDto dto = e.toDto();
                dtos.add(dto);
            }

            expenseResponse = new ExpenseResponse(dtos);
        } catch (Exception e) {
            expenseResponse.createErrorResponse("", "");
            return expenseResponse;
        }

        expenseResponse.createResponse();

        return expenseResponse;
    }
}
