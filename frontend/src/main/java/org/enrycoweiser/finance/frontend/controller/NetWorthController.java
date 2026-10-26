package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.NetWorthRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

public class NetWorthController {
    public static NetWorthRequest createAlterJson(Map<String, Object> data) {
        if(data == null || data.isEmpty()) {
            return null;
        }

        return assignValuesRequest(data);
    }

    public static NetWorthRequest createRefreshJson(Map<String, Object> data) {
        return assignValuesRequest(data);
    }

    public static NetWorthRequest createDeleteJson(Long id) {
        NetWorthRequest req = new NetWorthRequest();
        req.setId(id);

        return req;
    }

    public static NetWorthRequest assignValuesRequest(Map<String, Object> data) {
        NetWorthRequest req = new NetWorthRequest();

        if(data.get(FilterUtils.ID) instanceof Long l) {
            req.setId(l);
        }

        if(data.get(FilterUtils.NET_WORTH_DATE) instanceof LocalDate d) {
            req.setDate(d);
        }

        if(data.get(FilterUtils.NET_WORTH_DATE_FROM) instanceof LocalDate d) {
            req.setDateFrom(d);
        }

        if(data.get(FilterUtils.NET_WORTH_DATE_TO) instanceof LocalDate d) {
            req.setDateTo(d);
        }

        return req;
    }

    public static NetWorthResponse callAlterAPI(Map<String, Object> data) {
        NetWorthRequest req = createAlterJson(data);

        if(req == null) {
            return null;
        }

        String url = APIUtils.API_ENTITY_NET_WORTH_SAVE;

        return callAPI(req, url);
    }

    public static NetWorthResponse callRefreshAPI(Map<String, Object> data) {
        NetWorthRequest req = createRefreshJson(data);

        String url = APIUtils.API_ENTITY_NET_WORTH_REFRESH;

        return callAPI(req, url);
    }

    public static NetWorthResponse callDeleteAPI(Long id) {
        NetWorthRequest req = createDeleteJson(id);

        String url = APIUtils.API_ENTITY_NET_WORTH_DELETE;

        return callAPI(req, url);
    }

    private static NetWorthResponse callAPI(NetWorthRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        NetWorthResponse res = new NetWorthResponse();
        try {
            ResponseEntity<NetWorthResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    NetWorthResponse.class
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
