package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferDto extends StandardDto {
    protected LocalDate date;
    protected PaymentMethodDto fromPaymentMethodDto;
    protected PaymentMethodDto toPaymentMethodDto;
    protected BigDecimal money;
    protected String note;
    protected Long userId;
}
