package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.domain.Investment;
import org.enrycoweiser.finance.backend.domain.InvestmentHistory;
import org.enrycoweiser.finance.backend.repository.InvestmentHistoryRepository;
import org.enrycoweiser.finance.backend.repository.InvestmentRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.InvestmentHistoryFilter;
import org.enrycoweiser.finance.shared.api.request.InvestmentHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentHistoryResponse;
import org.enrycoweiser.finance.shared.dto.InvestmentHistoryDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvestmentHistoryService implements EntityService<InvestmentHistoryDto,
                                                                InvestmentHistoryRequest,
                                                                InvestmentHistoryResponse> {

    private final InvestmentHistoryRepository investmentHistoryRepository;
    private final InvestmentRepository investmentRepository;

    @Transactional
    @Override
    public InvestmentHistoryResponse save(InvestmentHistoryRequest request) {
        InvestmentHistory h;
        InvestmentHistoryResponse historyResponse = new InvestmentHistoryResponse();
        Optional<InvestmentHistory> history;

        try {
            history = investmentHistoryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return historyResponse;
        }

        if(history.isPresent()) {
            h = history.get();
        } else {
            Optional<Investment> investment = investmentRepository.retrieveById(request.getInvestmentId());
            if(investment.isEmpty()) {
                historyResponse.createErrorResponse(ErrorUtils.API_009, ErrorUtils.API_009_CODE);
                return historyResponse;
            }

            h = new InvestmentHistory();
            h.setInvestment(investment.get());
            h.setUserId(SEManager.getId());
        }

        if(StaticUtils.INVESTMENT_SELL.equals(request.getType())
                && request.getShares() != null
                && request.getShares().compareTo(heldShares(h)) > 0) {
            historyResponse.createErrorResponse(ErrorUtils.API_010, ErrorUtils.API_010_CODE);
            return historyResponse;
        }

        h.setDate(request.getDate());
        h.setType(request.getType());
        h.setAmount(request.getAmount());
        h.setFee(request.getFee());
        h.setShares(request.getShares());
        if(request.getPaymentMethod() == null) {
            historyResponse.createErrorResponse(ErrorUtils.API_004, ErrorUtils.API_004_CODE);
            return historyResponse;
        }
        h.setPaymentMethod(new PaymentMethod(request.getPaymentMethod()));
        h.setNote(request.getNote());

        try {
            investmentHistoryRepository.save(h);
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return historyResponse;
        }

        historyResponse.createResponse();

        return historyResponse;
    }

    @Transactional
    @Override
    public InvestmentHistoryResponse delete(InvestmentHistoryRequest request) {
        InvestmentHistoryResponse historyResponse = new InvestmentHistoryResponse();
        Optional<InvestmentHistory> history;

        try {
            history = investmentHistoryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return historyResponse;
        }

        if(history.isPresent()) {
            try {
                investmentHistoryRepository.delete(history.get());
            } catch (Exception ex) {
                historyResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return historyResponse;
            }
        } else {
            historyResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return historyResponse;
        }

        historyResponse.createResponse();

        return historyResponse;
    }

    @Transactional
    @Override
    public InvestmentHistoryResponse retrieve(InvestmentHistoryRequest request) {
        InvestmentHistoryResponse historyResponse = new InvestmentHistoryResponse();
        Map<String, Object> filters = InvestmentHistoryFilter.createFilterMap(request);

        try {
            List<InvestmentHistory> histories = investmentHistoryRepository.retrieveByFilters(filters);

            List<InvestmentHistoryDto> dtos = new ArrayList<>();
            for (InvestmentHistory h : histories) {
                InvestmentHistoryDto dto = h.toDto();
                dtos.add(dto);
            }

            historyResponse = new InvestmentHistoryResponse(dtos);
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return historyResponse;
        }

        historyResponse.createResponse();

        return historyResponse;
    }

    /*
        Shares held of the investment of this row, without counting the row itself
        (so editing an existing sell does not count it twice).
     */
    protected BigDecimal heldShares(InvestmentHistory current) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.INVESTMENT_HISTORY_INVESTMENT, current.getInvestment().getId());

        BigDecimal held = BigDecimal.ZERO;
        for (InvestmentHistory h : investmentHistoryRepository.retrieveByFilters(filters)) {
            if(h.getShares() == null || Objects.equals(h.getId(), current.getId())) {
                continue;
            }

            if(h.isBuy()) {
                held = held.add(h.getShares());
            } else if(h.isSell()) {
                held = held.subtract(h.getShares());
            }
        }

        return held;
    }
}
