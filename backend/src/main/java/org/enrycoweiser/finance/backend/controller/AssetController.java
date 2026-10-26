package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.AssetService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.AssetRequest;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/asset")
public class AssetController extends EntityController<AssetDto,
                                                                AssetRequest,
                                                                AssetResponse,
                                                                AssetService> {
    public AssetController(AssetService service) {
        super(service);
    }
}
