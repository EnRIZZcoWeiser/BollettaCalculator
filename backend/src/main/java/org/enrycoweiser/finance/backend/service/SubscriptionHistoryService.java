package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.domain.Subscription;
import org.enrycoweiser.finance.backend.domain.SubscriptionHistory;
import org.enrycoweiser.finance.backend.repository.SubscriptionHistoryRepository;
import org.enrycoweiser.finance.backend.repository.SubscriptionRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.SubscriptionHistoryFilter;
import org.enrycoweiser.finance.shared.api.request.SubscriptionHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionHistoryResponse;
import org.enrycoweiser.finance.shared.dto.SubscriptionHistoryDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionHistoryService implements EntityService<SubscriptionHistoryDto,
                                                                SubscriptionHistoryRequest,
                                                                SubscriptionHistoryResponse> {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    @Override
    public SubscriptionHistoryResponse save(SubscriptionHistoryRequest request) {
        SubscriptionHistory h;
        SubscriptionHistoryResponse historyResponse = new SubscriptionHistoryResponse();
        Optional<SubscriptionHistory> history;

        try {
            history = subscriptionHistoryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return historyResponse;
        }

        if(history.isPresent()) {
            h = history.get();
        } else {
            Optional<Subscription> subscription = subscriptionRepository.retrieveById(request.getSubscriptionId());
            if(subscription.isEmpty()) {
                historyResponse.createErrorResponse(ErrorUtils.API_007, ErrorUtils.API_007_CODE);
                return historyResponse;
            }

            h = new SubscriptionHistory();
            h.setSubscription(subscription.get());
            h.setUserId(SEManager.getId());
        }

        h.setStartDate(request.getStartDate());
        h.setEndDate(request.getEndDate());
        h.setPrice(request.getPrice());
        if(request.getPaymentMethod() == null) {
            historyResponse.createErrorResponse(ErrorUtils.API_004, ErrorUtils.API_004_CODE);
            return historyResponse;
        }
        h.setPaymentMethod(new PaymentMethod(request.getPaymentMethod()));

        try {
            subscriptionHistoryRepository.save(h);
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return historyResponse;
        }

        historyResponse.createResponse();

        return historyResponse;
    }

    @Transactional
    @Override
    public SubscriptionHistoryResponse delete(SubscriptionHistoryRequest request) {
        SubscriptionHistoryResponse historyResponse = new SubscriptionHistoryResponse();
        Optional<SubscriptionHistory> history;

        try {
            history = subscriptionHistoryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return historyResponse;
        }

        if(history.isPresent()) {
            try {
                subscriptionHistoryRepository.delete(history.get());
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
    public SubscriptionHistoryResponse retrieve(SubscriptionHistoryRequest request) {
        SubscriptionHistoryResponse historyResponse = new SubscriptionHistoryResponse();
        Map<String, Object> filters = SubscriptionHistoryFilter.createFilterMap(request);

        try {
            List<SubscriptionHistory> histories = subscriptionHistoryRepository.retrieveByFilters(filters);

            List<SubscriptionHistoryDto> dtos = new ArrayList<>();
            for (SubscriptionHistory h : histories) {
                SubscriptionHistoryDto dto = h.toDto();
                dtos.add(dto);
            }

            historyResponse = new SubscriptionHistoryResponse(dtos);
        } catch (Exception ex) {
            historyResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return historyResponse;
        }

        historyResponse.createResponse();

        return historyResponse;
    }
}
