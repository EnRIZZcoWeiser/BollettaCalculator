package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest extends StandardRequest {
    protected LocalDate date;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    private BigDecimal money;
    private String location;
    private String store;
    private String category;
    private PaymentMethodDto paymentMethod;
    private String note;
    private Long userId;
}
