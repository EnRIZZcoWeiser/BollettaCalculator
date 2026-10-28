package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.InvestmentHistoryService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.InvestmentHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentHistoryResponse;
import org.enrycoweiser.finance.shared.dto.InvestmentHistoryDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/investmentHistory")
public class InvestmentHistoryController extends EntityController<InvestmentHistoryDto,
                                                        InvestmentHistoryRequest,
                                                        InvestmentHistoryResponse,
                                                        InvestmentHistoryService> {
    public InvestmentHistoryController(InvestmentHistoryService service) {
        super(service);
    }
}
