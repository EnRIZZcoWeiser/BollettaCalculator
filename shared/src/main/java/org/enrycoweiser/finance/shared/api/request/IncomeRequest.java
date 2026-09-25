package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IncomeRequest extends StandardRequest {
    protected LocalDate date;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected BigDecimal money;
    protected String category;
    protected PaymentMethodDto paymentMethod;
    protected String note;
    protected Long userId;
}
