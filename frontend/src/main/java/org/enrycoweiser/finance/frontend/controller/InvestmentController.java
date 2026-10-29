package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.InvestmentRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InvestmentController {
    public static InvestmentRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static InvestmentRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static InvestmentRequest createDeleteJson(Long id) {
        InvestmentRequest req = new InvestmentRequest();
        req.setId(id);

        return req;
    }

    public static InvestmentRequest assignValuesRequest(Map<String, Object> data) {
        InvestmentRequest req = new InvestmentRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.INVESTMENT_ASSET) instanceof AssetDto a) {
            req.setAsset(a);
        }

        if(data.get(FilterUtils.INVESTMENT_NAME) instanceof String s) {
            req.setName(s);
        }

        if(data.get(FilterUtils.INVESTMENT_CATEGORY) instanceof String s) {
            req.setCategory(s);
        }

        if(data.get(FilterUtils.INVESTMENT_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        if(data.get(FilterUtils.INVESTMENT_ONLY_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        return req;
    }

    public static InvestmentResponse callAlterAPI(Map<String, Object> data) {
        InvestmentRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_INVESTMENT_SAVE;

        return callAPI(req, url);
    }

    public static InvestmentResponse callRefreshAPI(Map<String, Object> data) {
        InvestmentRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_INVESTMENT_REFRESH;

        return callAPI(req, url);
    }

    public static InvestmentResponse callDeleteAPI(Long id) {
        InvestmentRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_INVESTMENT_DELETE;

        return callAPI(req, url);
    }

    private static InvestmentResponse callAPI(InvestmentRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        InvestmentResponse res = new InvestmentResponse();
        try {
            ResponseEntity<InvestmentResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    InvestmentResponse.class
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
