package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.NetWorthEntryRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class NetWorthEntryFilter {
    public static Map<String, Object> createFilterMap(NetWorthEntryRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getNetWorthId() != null) {
            filterMap.put(FilterUtils.NET_WORTH_ENTRY_NET_WORTH, request.getNetWorthId());
        }

        if(request.getAsset() != null) {
            filterMap.put(FilterUtils.NET_WORTH_ENTRY_ASSET, request.getAsset());
        }

        if(request.getCategory() != null && !request.getCategory().isBlank()) {
            filterMap.put(FilterUtils.NET_WORTH_ENTRY_CATEGORY, request.getCategory());
        }

        return filterMap;
    }
}
