package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.ExpenseRequest;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ExpenseController {
    public static ExpenseRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static ExpenseRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static ExpenseRequest createDeleteJson(Long id) {
        ExpenseRequest req = new ExpenseRequest();
        req.setId(id);

        return req;
    }

    public static ExpenseRequest assignValuesRequest(Map<String, Object> data) {
        ExpenseRequest req = new ExpenseRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.EXPENSE_DATE) instanceof LocalDate d) {
            req.setDate(d);
        }

        if(data.get(FilterUtils.EXPENSE_DATE_TO) instanceof LocalDate d) {
            req.setDateTo(d);
        }

        if(data.get(FilterUtils.EXPENSE_DATE_FROM) instanceof LocalDate d) {
            req.setDateFrom(d);
        }

        if(data.get(FilterUtils.EXPENSE_MONEY) instanceof BigDecimal b) {
            req.setMoney(b);
        }

        if(data.get(FilterUtils.EXPENSE_LOCATION) instanceof String s) {
            req.setLocation(s);
        }

        if(data.get(FilterUtils.EXPENSE_STORE) instanceof String s) {
            req.setStore(s);
        }

        if(data.get(FilterUtils.EXPENSE_CATEGORY) instanceof String s) {
            req.setCategory(s);
        }

        if(data.get(FilterUtils.EXPENSE_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            req.setPaymentMethod(p);
        }

        if(data.get(FilterUtils.EXPENSE_NOTE) instanceof String s) {
            req.setNote(s);
        }

        return req;
    }

    public static ExpenseResponse callAlterAPI(Map<String, Object> data) {
        ExpenseRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_EXPENSE_SAVE;

        return callAPI(req, url);
    }

    public static ExpenseResponse callRefreshAPI(Map<String, Object> data) {
        ExpenseRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_EXPENSE_REFRESH;

        return callAPI(req, url);
    }

    public static ExpenseResponse callDeleteAPI(Long id) {
        ExpenseRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_EXPENSE_DELETE;

        return callAPI(req, url);
    }

    private static ExpenseResponse callAPI(ExpenseRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ExpenseResponse res = new ExpenseResponse();
        try {
            ResponseEntity<ExpenseResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    ExpenseResponse.class
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
