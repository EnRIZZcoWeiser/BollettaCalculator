package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvestmentHistoryRequest extends StandardRequest {
    protected Long investmentId;
    protected LocalDate date;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected String type;
    protected BigDecimal amount;
    protected BigDecimal fee;
    protected BigDecimal shares;
    protected PaymentMethodDto paymentMethod;
    protected String note;
    protected Long userId;
}
