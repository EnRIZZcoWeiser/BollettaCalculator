package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;

import java.util.List;

public class PaymentMethodResponse extends StandardResponse<PaymentMethodDto> {
    public PaymentMethodResponse() {
        super();
    }

    public PaymentMethodResponse(List<PaymentMethodDto> dtos) {
        super(dtos);
    }
}
