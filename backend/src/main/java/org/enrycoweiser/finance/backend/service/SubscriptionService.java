package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Subscription;
import org.enrycoweiser.finance.backend.domain.SubscriptionHistory;
import org.enrycoweiser.finance.backend.repository.SubscriptionHistoryRepository;
import org.enrycoweiser.finance.backend.repository.SubscriptionRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.SubscriptionFilter;
import org.enrycoweiser.finance.shared.api.request.SubscriptionRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionResponse;
import org.enrycoweiser.finance.shared.dto.SubscriptionDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements EntityService<SubscriptionDto,
        SubscriptionRequest,
        SubscriptionResponse> {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Transactional
    @Override
    public SubscriptionResponse save(SubscriptionRequest request) {
        Subscription s;
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        Optional<Subscription> subscription;

        try {
            subscription = subscriptionRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            subscriptionResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return subscriptionResponse;
        }

        if (subscription.isPresent()) {
            s = subscription.get();
        } else {
            s = new Subscription();
            s.setUserId(SEManager.getId());
        }

        s.setName(request.getName());
        s.setActive(request.getActive());

        try {
            subscriptionRepository.save(s);
        } catch (Exception ex) {
            subscriptionResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return subscriptionResponse;
        }

        subscriptionResponse.createResponse();

        return subscriptionResponse;
    }

    @Transactional
    @Override
    public SubscriptionResponse delete(SubscriptionRequest request) {
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        Optional<Subscription> subscription;

        try {
            subscription = subscriptionRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            subscriptionResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return subscriptionResponse;
        }

        if (subscription.isPresent()) {
            try {
                subscriptionHistoryRepository.deleteAll(retrieveHistories(subscription.get().getId()));
                subscriptionRepository.delete(subscription.get());
            } catch (Exception ex) {
                subscriptionResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return subscriptionResponse;
            }
        } else {
            subscriptionResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return subscriptionResponse;
        }

        subscriptionResponse.createResponse();

        return subscriptionResponse;
    }

    @Transactional
    @Override
    public SubscriptionResponse retrieve(SubscriptionRequest request) {
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        Map<String, Object> filters = SubscriptionFilter.createFilterMap(request);

        try {
            List<Subscription> subscriptions = subscriptionRepository.retrieveByFilters(filters);

            List<SubscriptionDto> dtos = new ArrayList<>();
            for (Subscription s : subscriptions) {
                SubscriptionDto dto = s.toDto();
                calculateCurrent(dto);
                dtos.add(dto);
            }

            subscriptionResponse = new SubscriptionResponse(dtos);
        } catch (Exception ex) {
            subscriptionResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return subscriptionResponse;
        }

        subscriptionResponse.createResponse();

        return subscriptionResponse;
    }

    protected List<SubscriptionHistory> retrieveHistories(Long subscriptionId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION, subscriptionId);

        return subscriptionHistoryRepository.retrieveByFilters(filters);
    }

    /*
        Price and monthly amount come from the history row active today.
        If none is active, the most recent one is used (rows are ordered by start date desc).
     */
    protected void calculateCurrent(SubscriptionDto dto) {
        List<SubscriptionHistory> histories = retrieveHistories(dto.getId());
        if(histories.isEmpty()) {
            return;
        }

        LocalDate today = LocalDate.now();

        SubscriptionHistory current = histories.get(0);
        for (SubscriptionHistory h : histories) {
            if(h.isActiveOn(today)) {
                current = h;
                break;
            }
        }

        dto.setPrice(current.getPrice());
        dto.setMonthlyAmount(current.monthlyAmount());
    }
}
