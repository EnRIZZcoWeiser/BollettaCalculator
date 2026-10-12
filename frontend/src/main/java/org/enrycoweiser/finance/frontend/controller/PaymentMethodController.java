package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.PaymentMethodRequest;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class PaymentMethodController {
    public static PaymentMethodRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static PaymentMethodRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static PaymentMethodRequest createDeleteJson(Long id) {
        PaymentMethodRequest req = new PaymentMethodRequest();
        req.setId(id);

        return req;
    }

    public static PaymentMethodRequest assignValuesRequest(Map<String, Object> data) {
        PaymentMethodRequest req = new PaymentMethodRequest();

        if(data.get(FilterUtils.PAYMENT_METHOD_NAME) instanceof String s) {
            req.setName(s);
        }

        if(data.get(FilterUtils.PAYMENT_METHOD_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        return req;
    }

    public static PaymentMethodResponse callAlterAPI(Map<String, Object> data) {
        PaymentMethodRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_PAYMENT_METHOD_SAVE;

        return callAPI(req, url);
    }

    public static PaymentMethodResponse callRefreshAPI(Map<String, Object> data) {
        PaymentMethodRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_PAYMENT_METHOD_REFRESH;

        return callAPI(req, url);
    }

    public static PaymentMethodResponse callDeleteAPI(Long id) {
        PaymentMethodRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_PAYMENT_METHOD_DELETE;

        return callAPI(req, url);
    }

    private static PaymentMethodResponse callAPI(PaymentMethodRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentMethodResponse res = new PaymentMethodResponse();
        try {
            ResponseEntity<PaymentMethodResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    PaymentMethodResponse.class
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
