package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NetWorthDto extends StandardDto {
    protected LocalDate date;
    protected Long userId;

    /* calculated */
    protected BigDecimal total;
    protected BigDecimal investments;
    protected BigDecimal spending;
}
