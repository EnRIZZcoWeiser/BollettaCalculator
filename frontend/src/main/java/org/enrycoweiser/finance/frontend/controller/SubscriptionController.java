package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.SubscriptionRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class SubscriptionController {
    public static SubscriptionRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static SubscriptionRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static SubscriptionRequest createDeleteJson(Long id) {
        SubscriptionRequest req = new SubscriptionRequest();
        req.setId(id);

        return req;
    }

    public static SubscriptionRequest assignValuesRequest(Map<String, Object> data) {
        SubscriptionRequest req = new SubscriptionRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_NAME) instanceof String s) {
            req.setName(s);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        if(data.get(FilterUtils.SUBSCRIPTION_ONLY_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        return req;
    }

    public static SubscriptionResponse callAlterAPI(Map<String, Object> data) {
        SubscriptionRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_SAVE;

        return callAPI(req, url);
    }

    public static SubscriptionResponse callRefreshAPI(Map<String, Object> data) {
        SubscriptionRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_REFRESH;

        return callAPI(req, url);
    }

    public static SubscriptionResponse callDeleteAPI(Long id) {
        SubscriptionRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_SUBSCRIPTION_DELETE;

        return callAPI(req, url);
    }

    private static SubscriptionResponse callAPI(SubscriptionRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SubscriptionResponse res = new SubscriptionResponse();
        try {
            ResponseEntity<SubscriptionResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    SubscriptionResponse.class
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
