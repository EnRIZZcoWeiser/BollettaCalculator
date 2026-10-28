package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.SubscriptionHistoryDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class SubscriptionHistoryResponse extends StandardResponse<SubscriptionHistoryDto> {
    public SubscriptionHistoryResponse() {
        super();
    }

    public SubscriptionHistoryResponse(List<SubscriptionHistoryDto> dtos) {
        super(dtos);
    }
}
