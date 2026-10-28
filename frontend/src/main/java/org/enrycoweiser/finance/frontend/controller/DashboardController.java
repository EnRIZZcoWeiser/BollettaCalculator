package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.DashboardRequest;
import org.enrycoweiser.finance.shared.api.response.DashboardResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class DashboardController {
    public static DashboardRequest assignValuesRequest(Map<String, Object> data) {
        DashboardRequest req = new DashboardRequest();

        if(data.get(FilterUtils.DASHBOARD_YEAR) instanceof Integer i) {
            req.setYear(i);
        }

        if(data.get(FilterUtils.DASHBOARD_MONTH) instanceof Integer i) {
            req.setMonth(i);
        }

        return req;
    }

    public static DashboardResponse callSummaryAPI(Map<String, Object> data) {
        DashboardRequest req = assignValuesRequest(data);

        String url = APIUtils.API_DASHBOARD_SUMMARY;

        return callAPI(req, url);
    }

    private static DashboardResponse callAPI(DashboardRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DashboardResponse res = new DashboardResponse();
        try {
            ResponseEntity<DashboardResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    DashboardResponse.class
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
