package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.SubscriptionHistoryService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.SubscriptionHistoryRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionHistoryResponse;
import org.enrycoweiser.finance.shared.dto.SubscriptionHistoryDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/subscriptionHistory")
public class SubscriptionHistoryController extends EntityController<SubscriptionHistoryDto,
                                                        SubscriptionHistoryRequest,
                                                        SubscriptionHistoryResponse,
                                                        SubscriptionHistoryService> {
    public SubscriptionHistoryController(SubscriptionHistoryService service) {
        super(service);
    }
}
