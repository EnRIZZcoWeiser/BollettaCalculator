package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.InvestmentHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentHistoryResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class InvestmentHistoryController {
    public static InvestmentHistoryRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static InvestmentHistoryRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static InvestmentHistoryRequest createDeleteJson(Long id) {
        InvestmentHistoryRequest req = new InvestmentHistoryRequest();
        req.setId(id);

        return req;
    }

    public static InvestmentHistoryRequest assignValuesRequest(Map<String, Object> data) {
        InvestmentHistoryRequest req = new InvestmentHistoryRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_INVESTMENT) instanceof Long l) {
            req.setInvestmentId(l);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_DATE) instanceof LocalDate d) {
            req.setDate(d);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_DATE_FROM) instanceof LocalDate d) {
            req.setDateFrom(d);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_DATE_TO) instanceof LocalDate d) {
            req.setDateTo(d);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_TYPE) instanceof String s) {
            req.setType(s);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_AMOUNT) instanceof BigDecimal b) {
            req.setAmount(b);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_FEE) instanceof BigDecimal b) {
            req.setFee(b);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_SHARES) instanceof BigDecimal b) {
            req.setShares(b);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            req.setPaymentMethod(p);
        }

        if(data.get(FilterUtils.INVESTMENT_HISTORY_NOTE) instanceof String s) {
            req.setNote(s);
        }

        return req;
    }

    public static InvestmentHistoryResponse callAlterAPI(Map<String, Object> data) {
        InvestmentHistoryRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_INVESTMENT_HISTORY_SAVE;

        return callAPI(req, url);
    }

    public static InvestmentHistoryResponse callRefreshAPI(Map<String, Object> data) {
        InvestmentHistoryRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_INVESTMENT_HISTORY_REFRESH;

        return callAPI(req, url);
    }

    public static InvestmentHistoryResponse callDeleteAPI(Long id) {
        InvestmentHistoryRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_INVESTMENT_HISTORY_DELETE;

        return callAPI(req, url);
    }

    private static InvestmentHistoryResponse callAPI(InvestmentHistoryRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        InvestmentHistoryResponse res = new InvestmentHistoryResponse();
        try {
            ResponseEntity<InvestmentHistoryResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    InvestmentHistoryResponse.class
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
