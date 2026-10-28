package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.SubscriptionHistoryRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionHistoryFilter {
    public static Map<String, Object> createFilterMap(SubscriptionHistoryRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getSubscriptionId() != null) {
            filterMap.put(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION, request.getSubscriptionId());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD, request.getPaymentMethod());
        }

        return filterMap;
    }
}
