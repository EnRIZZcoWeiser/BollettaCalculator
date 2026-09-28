package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.api.response.IncomeResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class IncomeController {
    public static IncomeRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static IncomeRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static IncomeRequest createDeleteJson(Long id) {
        IncomeRequest req = new IncomeRequest();
        req.setId(id);

        return req;
    }

    public static IncomeRequest assignValuesRequest(Map<String, Object> data) {
        IncomeRequest req = new IncomeRequest();

        if(data.get(FilterUtils.INCOME_DATE) instanceof LocalDate d) {
            req.setDate(d);
        }

        if(data.get(FilterUtils.INCOME_MONEY) instanceof BigDecimal b) {
            req.setMoney(b);
        }

        if(data.get(FilterUtils.INCOME_CATEGORY) instanceof String s) {
            req.setCategory(s);
        }

        if(data.get(FilterUtils.INCOME_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            req.setPaymentMethod(p);
        }

        if(data.get(FilterUtils.INCOME_NOTE) instanceof String s) {
            req.setNote(s);
        }

        return req;
    }

    public static IncomeResponse callAlterAPI(Map<String, Object> data) {
        IncomeRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_INCOME_SAVE;

        return callAPI(req, url);
    }

    public static IncomeResponse callRefreshAPI(Map<String, Object> data) {
        IncomeRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_INCOME_REFRESH;

        return callAPI(req, url);
    }

    public static IncomeResponse callDeleteAPI(Long id) {
        IncomeRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_INCOME_DELETE;

        return callAPI(req, url);
    }

    private static IncomeResponse callAPI(IncomeRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        IncomeResponse res = new IncomeResponse();
        try {
            ResponseEntity<IncomeResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    IncomeResponse.class
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
