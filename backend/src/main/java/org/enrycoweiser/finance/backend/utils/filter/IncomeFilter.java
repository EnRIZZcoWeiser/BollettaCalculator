package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IncomeFilter {
    public static Map<String, Object> createFilterMap(IncomeRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getDate() != null) {
            filterMap.put(FilterUtils.INCOME_DATE, request.getDate());
        }

        if(request.getMoney() != null && request.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            filterMap.put(FilterUtils.INCOME_MONEY, request.getMoney());
        }

        if(request.getCategory() != null && !request.getCategory().isEmpty()) {
            filterMap.put(FilterUtils.INCOME_CATEGORY, request.getCategory());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.INCOME_PAYMENT_METHOD, request.getPaymentMethod());
        }

        return filterMap;
    }
}
