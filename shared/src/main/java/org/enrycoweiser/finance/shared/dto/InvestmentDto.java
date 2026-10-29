package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentDto extends StandardDto {
    protected AssetDto assetDto;
    protected Boolean active;
    protected Long userId;

    /* taken from the asset */
    protected String name;
    protected String category;

    /* calculated from the history rows */
    protected BigDecimal shares;
    protected BigDecimal averagePrice;
    protected BigDecimal invested;
    protected BigDecimal fees;

    @Override
    public String toString() {
        return this.name;
    }
}
