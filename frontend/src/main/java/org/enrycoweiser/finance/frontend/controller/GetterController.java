package org.enrycoweiser.finance.frontend.controller;

import org.enrycoweiser.finance.frontend.utils.APIUtils;
import org.enrycoweiser.finance.frontend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class GetterController {
    public static PaymentMethodResponse callGetAllPaymentMethodsAPI() {
        String url = APIUtils.API_LIST_PAYMENT_METHOD;
        return callPaymentMethodAPI(url);
    }

    public static AssetResponse callGetAllAssetsAPI() {
        String url = APIUtils.API_LIST_ASSET;
        return callAssetAPI(url);
    }

    private static PaymentMethodResponse callPaymentMethodAPI(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentMethodResponse res = new PaymentMethodResponse();

        try {
            ResponseEntity<PaymentMethodResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
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

    private static AssetResponse callAssetAPI(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AssetResponse res = new AssetResponse();

        try {
            ResponseEntity<AssetResponse> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
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
