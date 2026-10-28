package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.TransferRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class TransferFilter {
    public static Map<String, Object> createFilterMap(TransferRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getDateFrom() != null) {
            filterMap.put(FilterUtils.TRANSFER_DATE_FROM, request.getDateFrom());
        }

        if(request.getDateTo() != null) {
            filterMap.put(FilterUtils.TRANSFER_DATE_TO, request.getDateTo());
        }

        if(request.getFromPaymentMethod() != null) {
            filterMap.put(FilterUtils.TRANSFER_FROM, request.getFromPaymentMethod());
        }

        if(request.getToPaymentMethod() != null) {
            filterMap.put(FilterUtils.TRANSFER_TO, request.getToPaymentMethod());
        }

        if(request.getPaymentMethod() != null) {
            filterMap.put(FilterUtils.TRANSFER_PAYMENT_METHOD, request.getPaymentMethod());
        }

        return filterMap;
    }
}
