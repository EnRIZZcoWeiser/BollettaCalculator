package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
    Balance of a single payment method at today's date.
 */
@Getter
@Setter
public class LiquidityDto {
    protected String paymentMethod;
    protected BigDecimal balance;
}
