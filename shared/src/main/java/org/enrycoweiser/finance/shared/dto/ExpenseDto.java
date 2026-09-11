package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ExpenseDto extends StandardDto {
    protected Date date;
    protected BigDecimal money;
    protected String location;
    protected String store;
    protected String category;
    protected PaymentMethodDto paymentMethod;
    protected String note;
    protected Long userId;
}
