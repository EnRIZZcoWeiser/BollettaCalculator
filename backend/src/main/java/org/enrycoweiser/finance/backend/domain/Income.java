package org.enrycoweiser.finance.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Income extends StandardEntity {
    private Date date;
    private BigDecimal money;
    private String category;
    private PaymentMethod paymentMethod;
    private String note;
    private Long userId;
}
