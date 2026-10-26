package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.AssetRepository;
import org.enrycoweiser.finance.backend.repository.PaymentMethodRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetterService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final AssetRepository assetRepository;

    @Transactional
    public PaymentMethodResponse getAllPaymentMethods() {
        PaymentMethodResponse pmResponse = new PaymentMethodResponse();

        try {
            List<PaymentMethod> pms = paymentMethodRepository.retrieveByUser(SEManager.getId());

            List<PaymentMethodDto> dtos = new ArrayList<>();
            for (PaymentMethod pm : pms) {
                PaymentMethodDto dto = pm.toDto();
                dtos.add(dto);
            }

            pmResponse = new PaymentMethodResponse(dtos);
        } catch (Exception ex) {
            pmResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return pmResponse;
        }

        pmResponse.createResponse();

        return pmResponse;
    }

    @Transactional
    public AssetResponse getAllAssets() {
        AssetResponse assetResponse = new AssetResponse();

        try {
            List<Asset> assets = assetRepository.retrieveByUser(SEManager.getId());

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
