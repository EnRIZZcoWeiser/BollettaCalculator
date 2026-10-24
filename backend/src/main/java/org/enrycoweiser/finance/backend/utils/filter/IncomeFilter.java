package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class IncomeFilter {
    public static Map<String, Object> createFilterMap(IncomeRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getDateFrom() != null) {
            filterMap.put(FilterUtils.INCOME_DATE_FROM, request.getDateFrom());
        }

        if(request.getDateTo() != null) {
            filterMap.put(FilterUtils.INCOME_DATE_TO, request.getDateTo());
        }

        if(request.getCategory() != null && !request.getCategory().isBlank()) {
            filterMap.put(FilterUtils.INCOME_CATEGORY, request.getCategory());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.INCOME_PAYMENT_METHOD, request.getPaymentMethod());
        }

        return filterMap;
    }
}
