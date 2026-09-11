package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class IncomeRequest extends StandardRequest {
    protected Date date;
    protected BigDecimal money;
    protected String category;
    protected PaymentMethodDto paymentMethod;
    protected String note;
    protected String userId;
}
