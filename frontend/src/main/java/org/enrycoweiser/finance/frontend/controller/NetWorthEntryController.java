package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.NetWorthEntryRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthEntryResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

public class NetWorthEntryController {
    public static NetWorthEntryRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static NetWorthEntryRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static NetWorthEntryRequest createDeleteJson(Long id) {
        NetWorthEntryRequest req = new NetWorthEntryRequest();
        req.setId(id);

        return req;
    }

    public static NetWorthEntryRequest assignValuesRequest(Map<String, Object> data) {
        NetWorthEntryRequest req = new NetWorthEntryRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.NET_WORTH_ENTRY_NET_WORTH) instanceof Long l) {
            req.setNetWorthId(l);
        }

        if(data.get(FilterUtils.NET_WORTH_ENTRY_ASSET) instanceof AssetDto a) {
            req.setAsset(a);
        }

        if(data.get(FilterUtils.NET_WORTH_ENTRY_CATEGORY) instanceof String s) {
            req.setCategory(s);
        }

        if(data.get(FilterUtils.NET_WORTH_ENTRY_VALUE) instanceof BigDecimal b) {
            req.setValue(b);
        }

        return req;
    }

    public static NetWorthEntryResponse callAlterAPI(Map<String, Object> data) {
        NetWorthEntryRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_NET_WORTH_ENTRY_SAVE;

        return callAPI(req, url);
    }

    public static NetWorthEntryResponse callRefreshAPI(Map<String, Object> data) {
        NetWorthEntryRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_NET_WORTH_ENTRY_REFRESH;

        return callAPI(req, url);
    }

    public static NetWorthEntryResponse callDeleteAPI(Long id) {
        NetWorthEntryRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_NET_WORTH_ENTRY_DELETE;

        return callAPI(req, url);
    }

    private static NetWorthEntryResponse callAPI(NetWorthEntryRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        NetWorthEntryResponse res = new NetWorthEntryResponse();
        try {
            ResponseEntity<NetWorthEntryResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    NetWorthEntryResponse.class
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
