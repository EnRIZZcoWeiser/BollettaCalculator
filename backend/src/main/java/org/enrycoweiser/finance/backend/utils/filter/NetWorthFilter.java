package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.NetWorthRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class NetWorthFilter {
    public static Map<String, Object> createFilterMap(NetWorthRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getId() != null) {
            filterMap.put(FilterUtils.ID, request.getId());
        }

        if(request.getDateFrom() != null) {
            filterMap.put(FilterUtils.NET_WORTH_DATE_FROM, request.getDateFrom());
        }

        if(request.getDateTo() != null) {
            filterMap.put(FilterUtils.NET_WORTH_DATE_TO, request.getDateTo());
        }

        return filterMap;
    }
}
