package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.SubscriptionDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;

import java.util.List;

public class SubscriptionResponse extends StandardResponse<SubscriptionDto> {
    public SubscriptionResponse() {
        super();
    }

    public SubscriptionResponse(List<SubscriptionDto> dtos) {
        super(dtos);
    }
}
