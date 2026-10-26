package org.enrycoweiser.finance.backend.utils.filter;

import org.enrycoweiser.finance.shared.api.request.AssetRequest;
import org.enrycoweiser.finance.shared.utils.FilterUtils;

import java.util.HashMap;
import java.util.Map;

public class AssetFilter {
    public static Map<String, Object> createFilterMap(AssetRequest request) {
        Map<String, Object> filterMap = new HashMap<>();

        if(request == null) {
            return filterMap;
        }

        if(request.getName() != null) {
            filterMap.put(FilterUtils.ASSET_NAME, request.getName());
        }

        if(request.getCategory() != null && !request.getCategory().isBlank()) {
            filterMap.put(FilterUtils.ASSET_CATEGORY, request.getCategory());
        }

        if(request.getActive() != null) {
            filterMap.put(FilterUtils.ASSET_ONLY_ACTIVE, request.getActive());
        }

        return filterMap;
    }
}
