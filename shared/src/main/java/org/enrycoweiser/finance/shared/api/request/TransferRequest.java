package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferRequest extends StandardRequest {
    protected LocalDate date;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected PaymentMethodDto fromPaymentMethod;
    protected PaymentMethodDto toPaymentMethod;
    /* filter only: transfers where the payment method is either the source or the destination */
    protected PaymentMethodDto paymentMethod;
    protected BigDecimal money;
    protected String note;
    protected Long userId;
}
