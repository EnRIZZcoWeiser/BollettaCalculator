package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionHistoryDto extends StandardDto {
    protected Long subscriptionId;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected BigDecimal price;
    protected BigDecimal monthlyAmount;
    protected PaymentMethodDto paymentMethodDto;
    protected Long userId;
}
