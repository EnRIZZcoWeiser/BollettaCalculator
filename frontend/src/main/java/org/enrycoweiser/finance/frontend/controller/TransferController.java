package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.TransferRequest;
import org.enrycoweiser.finance.shared.api.response.TransferResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class TransferController {
    public static TransferRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static TransferRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static TransferRequest createDeleteJson(Long id) {
        TransferRequest req = new TransferRequest();
        req.setId(id);

        return req;
    }

    public static TransferRequest assignValuesRequest(Map<String, Object> data) {
        TransferRequest req = new TransferRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.TRANSFER_DATE) instanceof LocalDate d) {
            req.setDate(d);
        }

        if(data.get(FilterUtils.TRANSFER_DATE_TO) instanceof LocalDate d) {
            req.setDateTo(d);
        }

        if(data.get(FilterUtils.TRANSFER_DATE_FROM) instanceof LocalDate d) {
            req.setDateFrom(d);
        }

        if(data.get(FilterUtils.TRANSFER_MONEY) instanceof BigDecimal b) {
            req.setMoney(b);
        }

        if(data.get(FilterUtils.TRANSFER_FROM) instanceof PaymentMethodDto p) {
            req.setFromPaymentMethod(p);
        }

        if(data.get(FilterUtils.TRANSFER_TO) instanceof PaymentMethodDto p) {
            req.setToPaymentMethod(p);
        }

        if(data.get(FilterUtils.TRANSFER_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            req.setPaymentMethod(p);
        }

        if(data.get(FilterUtils.TRANSFER_NOTE) instanceof String s) {
            req.setNote(s);
        }

        return req;
    }

    public static TransferResponse callAlterAPI(Map<String, Object> data) {
        TransferRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_TRANSFER_SAVE;

        return callAPI(req, url);
    }

    public static TransferResponse callRefreshAPI(Map<String, Object> data) {
        TransferRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_TRANSFER_REFRESH;

        return callAPI(req, url);
    }

    public static TransferResponse callDeleteAPI(Long id) {
        TransferRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_TRANSFER_DELETE;

        return callAPI(req, url);
    }

    private static TransferResponse callAPI(TransferRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TransferResponse res = new TransferResponse();
        try {
            ResponseEntity<TransferResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    TransferResponse.class
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
