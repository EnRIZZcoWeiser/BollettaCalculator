package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.PaymentMethodRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodFilter {
    public static Map<String, Object> createFilterMap(PaymentMethodRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getName() != null) {
            filterMap.put(FilterUtils.PAYMENT_METHOD_NAME, request.getName());
        }

        if(request.getActive() != null) {
            filterMap.put(FilterUtils.PAYMENT_METHOD_ACTIVE, request.getActive());
        }

        return filterMap;
    }
}
