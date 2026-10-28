package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.InvestmentService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.InvestmentRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/investment")
public class InvestmentController extends EntityController<InvestmentDto,
                                                                InvestmentRequest,
                                                                InvestmentResponse,
                                                                InvestmentService> {
    public InvestmentController(InvestmentService service) {
        super(service);
    }
}
