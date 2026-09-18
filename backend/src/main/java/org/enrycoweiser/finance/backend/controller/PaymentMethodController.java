package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.PaymentMethodService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.PaymentMethodRequest;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/paymentMethod")
public class PaymentMethodController extends EntityController<PaymentMethodDto,
                                                                PaymentMethodRequest,
                                                                PaymentMethodResponse,
                                                                PaymentMethodService> {
    public PaymentMethodController(PaymentMethodService service) {
        super(service);
    }
}
