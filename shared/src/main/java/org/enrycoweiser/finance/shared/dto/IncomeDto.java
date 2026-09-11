package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class IncomeDto extends StandardDto {
    protected Date date;
    protected BigDecimal money;
    protected String category;
    protected PaymentMethodDto paymentMethodDto;
    protected String note;
    protected String userId;
}
