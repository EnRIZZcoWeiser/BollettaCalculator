package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.IncomeService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.api.response.IncomeResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/income")
public class IncomeController extends EntityController<IncomeService, IncomeRequest, IncomeResponse> {
    public IncomeController(IncomeService service) {
        super(service);
    }
}
