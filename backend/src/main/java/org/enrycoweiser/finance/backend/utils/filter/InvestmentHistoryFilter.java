package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.InvestmentHistoryRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class InvestmentHistoryFilter {
    public static Map<String, Object> createFilterMap(InvestmentHistoryRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getInvestmentId() != null) {
            filterMap.put(FilterUtils.INVESTMENT_HISTORY_INVESTMENT, request.getInvestmentId());
        }

        if(request.getType() != null && !request.getType().isBlank()) {
            filterMap.put(FilterUtils.INVESTMENT_HISTORY_TYPE, request.getType());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD, request.getPaymentMethod());
        }

        if(request.getDateFrom() != null) {
            filterMap.put(FilterUtils.INVESTMENT_HISTORY_DATE_FROM, request.getDateFrom());
        }

        if(request.getDateTo() != null) {
            filterMap.put(FilterUtils.INVESTMENT_HISTORY_DATE_TO, request.getDateTo());
        }

        return filterMap;
    }
}
