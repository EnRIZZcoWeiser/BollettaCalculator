package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.ExpenseService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.ExpenseRequest;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.dto.ExpenseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/expense")
public class ExpenseController extends EntityController<ExpenseDto,
                                                        ExpenseRequest,
                                                        ExpenseResponse,
                                                        ExpenseService> {
    public ExpenseController(ExpenseService service) {
        super(service);
    }
}
