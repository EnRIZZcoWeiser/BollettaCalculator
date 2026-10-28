package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.InvestmentRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class InvestmentFilter {
    public static Map<String, Object> createFilterMap(InvestmentRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getName() != null) {
            filterMap.put(FilterUtils.INVESTMENT_NAME, request.getName());
        }

        if(request.getCategory() != null && !request.getCategory().isBlank()) {
            filterMap.put(FilterUtils.INVESTMENT_CATEGORY, request.getCategory());
        }

        if(request.getActive() != null) {
            filterMap.put(FilterUtils.INVESTMENT_ONLY_ACTIVE, request.getActive());
        }

        return filterMap;
    }
}
