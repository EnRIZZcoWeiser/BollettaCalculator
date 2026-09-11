package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.ExpenseRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExpenseFilter {
    public static Map<String, Object> createFilterMap(ExpenseRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getDate() != null) {
            filterMap.put(FilterUtils.EXPENSE_DATE, request.getDate());
        }

        if(request.getMoney() != null && request.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            filterMap.put(FilterUtils.EXPENSE_MONEY, request.getMoney());
        }

        if(request.getLocation() != null && !request.getLocation().isEmpty()) {
            filterMap.put(FilterUtils.EXPENSE_LOCATION, request.getLocation());
        }

        if(request.getStore() != null && !request.getStore().isEmpty()) {
            filterMap.put(FilterUtils.EXPENSE_STORE, request.getStore());
        }

        if(request.getCategory() != null && !request.getCategory().isEmpty()) {
            filterMap.put(FilterUtils.EXPENSE_CATEGORY, request.getCategory());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.EXPENSE_PAYMENT_METHOD, request.getPaymentMethod());
        }

        return filterMap;
    }
}
