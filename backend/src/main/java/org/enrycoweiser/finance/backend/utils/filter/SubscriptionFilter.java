package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.SubscriptionRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionFilter {
    public static Map<String, Object> createFilterMap(SubscriptionRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getName() != null) {
            filterMap.put(FilterUtils.SUBSCRIPTION_NAME, request.getName());
        }

        if(request.getActive() != null) {
            filterMap.put(FilterUtils.SUBSCRIPTION_ONLY_ACTIVE, request.getActive());
        }

        return filterMap;
    }
}
