package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.SubscriptionHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionHistoryResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class SubscriptionHistoryController {
    public static SubscriptionHistoryRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static SubscriptionHistoryRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static SubscriptionHistoryRequest createDeleteJson(Long id) {
        SubscriptionHistoryRequest req = new SubscriptionHistoryRequest();
        req.setId(id);

        return req;
    }

    public static SubscriptionHistoryRequest assignValuesRequest(Map<String, Object> data) {
        SubscriptionHistoryRequest req = new SubscriptionHistoryRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION) instanceof Long l) {
            req.setSubscriptionId(l);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE) instanceof LocalDate d) {
            req.setStartDate(d);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE) instanceof LocalDate d) {
            req.setEndDate(d);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_HISTORY_PRICE) instanceof BigDecimal b) {
            req.setPrice(b);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            req.setPaymentMethod(p);
        }

        return req;
    }

    public static SubscriptionHistoryResponse callAlterAPI(Map<String, Object> data) {
        SubscriptionHistoryRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_HISTORY_SAVE;

        return callAPI(req, url);
    }

    public static SubscriptionHistoryResponse callRefreshAPI(Map<String, Object> data) {
        SubscriptionHistoryRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_HISTORY_REFRESH;

        return callAPI(req, url);
    }

    public static SubscriptionHistoryResponse callDeleteAPI(Long id) {
        SubscriptionHistoryRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_HISTORY_DELETE;

        return callAPI(req, url);
    }

    private static SubscriptionHistoryResponse callAPI(SubscriptionHistoryRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SubscriptionHistoryResponse res = new SubscriptionHistoryResponse();
        try {
            ResponseEntity<SubscriptionHistoryResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    SubscriptionHistoryResponse.class
            );

            if(response.getBody() != null) {
                return response.getBody();
            }

            res.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return res;
        } catch (Exception e) {
            res.createErrorResponse(e.getMessage(), ErrorUtils.EX_001_CODE);
            return res;
        }
    }
}
