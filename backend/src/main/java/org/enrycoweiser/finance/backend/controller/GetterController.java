package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.GetterService;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
public class GetterController {

    private final GetterService service;

    public GetterController(GetterService service) {
        this.service = service;
    }

    @GetMapping("/paymentMethod")
    public PaymentMethodResponse getAllPaymentMethods() {
        return service.getAllPaymentMethods();
    }
}
