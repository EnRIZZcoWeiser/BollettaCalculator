package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseDto extends StandardDto {
    protected LocalDate date;
    protected BigDecimal money;
    protected String location;
    protected String store;
    protected String category;
    protected PaymentMethodDto paymentMethod;
    protected String note;
    protected Long userId;
}
