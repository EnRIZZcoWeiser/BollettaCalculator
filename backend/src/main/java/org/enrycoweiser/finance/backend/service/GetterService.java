package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.PaymentMethodRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetterService {

    private final PaymentMethodRepository paymentMethodRepository;

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
}
