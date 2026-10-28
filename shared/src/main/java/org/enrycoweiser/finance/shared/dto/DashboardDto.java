package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DashboardDto extends StandardDto {
    protected Integer year;
    protected Integer month;

    protected BigDecimal moneyIn;
    protected BigDecimal moneyOut;

    protected List<CategoryAmountDto> incomeCategories;
    protected List<CategoryAmountDto> expenseCategories;

    /* balance of every payment method at today's date, not tied to the selected month */
    protected List<LiquidityDto> liquidity;
}
