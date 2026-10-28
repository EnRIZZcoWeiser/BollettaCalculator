package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionHistoryRequest extends StandardRequest {
    protected Long subscriptionId;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected BigDecimal price;
    protected PaymentMethodDto paymentMethod;
    protected Long userId;
}
