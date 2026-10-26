package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.AssetRequest;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AssetController {
    public static AssetRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static AssetRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static AssetRequest createDeleteJson(Long id) {
        AssetRequest req = new AssetRequest();
        req.setId(id);

        return req;
    }

    public static AssetRequest assignValuesRequest(Map<String, Object> data) {
        AssetRequest req = new AssetRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.ASSET_NAME) instanceof String s) {
            req.setName(s);
        }

        if(data.get(FilterUtils.ASSET_CATEGORY) instanceof String s) {
            req.setCategory(s);
        }

        if(data.get(FilterUtils.ASSET_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        if(data.get(FilterUtils.ASSET_ONLY_ACTIVE) instanceof Boolean b) {
            req.setActive(b);
        }

        return req;
    }

    public static AssetResponse callAlterAPI(Map<String, Object> data) {
        AssetRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_ASSET_SAVE;

        return callAPI(req, url);
    }

    public static AssetResponse callRefreshAPI(Map<String, Object> data) {
        AssetRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_ASSET_REFRESH;

        return callAPI(req, url);
    }

    public static AssetResponse callDeleteAPI(Long id) {
        AssetRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_ASSET_DELETE;

        return callAPI(req, url);
    }

    private static AssetResponse callAPI(AssetRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AssetResponse res = new AssetResponse();
        try {
            ResponseEntity<AssetResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    AssetResponse.class
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
