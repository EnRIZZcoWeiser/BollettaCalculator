package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.repository.AssetRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.AssetFilter;
import org.enrycoweiser.finance.shared.api.request.AssetRequest;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService implements EntityService<AssetDto,
        AssetRequest,
        AssetResponse> {

    private final AssetRepository assetRepository;

    @Transactional
    @Override
    public AssetResponse save(AssetRequest request) {
        Asset a;
        AssetResponse assetResponse = new AssetResponse();
        Optional<Asset> asset;

        try {
            asset = assetRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            assetResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return assetResponse;
        }

        if (asset.isPresent()) {
            a = asset.get();
        } else {
            a = new Asset();
            a.setUserId(SEManager.getId());
        }

        a.setName(request.getName());
        a.setCategory(request.getCategory());
        a.setActive(request.getActive());

        try {
            assetRepository.save(a);
        } catch (Exception ex) {
            assetResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return assetResponse;
        }

        assetResponse.createResponse();

        return assetResponse;
    }

    @Transactional
    @Override
    public AssetResponse delete(AssetRequest request) {
        AssetResponse assetResponse = new AssetResponse();
        Optional<Asset> asset;

        try {
            asset = assetRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            assetResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return assetResponse;
        }

        if (asset.isPresent()) {
            try {
                assetRepository.delete(asset.get());
            } catch (Exception ex) {
                assetResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return assetResponse;
            }
        } else {
            assetResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return assetResponse;
        }

        assetResponse.createResponse();

        return assetResponse;
    }

    @Transactional
    @Override
    public AssetResponse retrieve(AssetRequest request) {
        AssetResponse assetResponse = new AssetResponse();
        Map<String, Object> filters = AssetFilter.createFilterMap(request);

        try {
            List<Asset> assets = assetRepository.retrieveByFilters(filters);

            List<AssetDto> dtos = new ArrayList<>();
            for (Asset a : assets) {
                AssetDto dto = a.toDto();
                dtos.add(dto);
            }

            assetResponse = new AssetResponse(dtos);
        } catch (Exception ex) {
            assetResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return assetResponse;
        }

        assetResponse.createResponse();

        return assetResponse;
    }
}
