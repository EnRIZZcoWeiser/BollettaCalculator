package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
    One line of a dashboard summary: total of a category and its share of the whole.
 */
@Getter
@Setter
public class CategoryAmountDto {
    protected String category;
    protected BigDecimal amount;
    protected BigDecimal percentage;
}
