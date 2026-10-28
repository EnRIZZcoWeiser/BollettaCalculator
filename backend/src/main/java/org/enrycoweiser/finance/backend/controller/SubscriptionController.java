package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.SubscriptionService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.SubscriptionRequest;
import org.enrycoweiser.finance.shared.api.response.SubscriptionResponse;
import org.enrycoweiser.finance.shared.dto.SubscriptionDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/subscription")
public class SubscriptionController extends EntityController<SubscriptionDto,
                                                                SubscriptionRequest,
                                                                SubscriptionResponse,
                                                                SubscriptionService> {
    public SubscriptionController(SubscriptionService service) {
        super(service);
    }
}
