package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;

import java.util.List;

public class AssetResponse extends StandardResponse<AssetDto> {
    public AssetResponse() {
        super();
    }

    public AssetResponse(List<AssetDto> dtos) {
        super(dtos);
    }
}
