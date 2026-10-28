package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.TransferService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.TransferRequest;
import org.enrycoweiser.finance.shared.api.response.TransferResponse;
import org.enrycoweiser.finance.shared.dto.TransferDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/transfer")
public class TransferController extends EntityController<TransferDto,
                                                        TransferRequest,
                                                        TransferResponse,
                                                        TransferService> {
    public TransferController(TransferService service) {
        super(service);
    }
}
