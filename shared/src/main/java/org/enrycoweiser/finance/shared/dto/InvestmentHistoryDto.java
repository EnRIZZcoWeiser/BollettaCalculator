package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvestmentHistoryDto extends StandardDto {
    protected Long investmentId;
    protected LocalDate date;
    protected String type;
    protected BigDecimal amount;
    protected BigDecimal fee;
    protected BigDecimal shares;
    protected PaymentMethodDto paymentMethodDto;
    protected String note;
    protected Long userId;

    /* calculated: amount / shares */
    protected BigDecimal pricePerShare;
}
