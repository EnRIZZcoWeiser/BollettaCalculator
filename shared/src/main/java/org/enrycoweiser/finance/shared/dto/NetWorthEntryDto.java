package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;

@Getter
@Setter
public class NetWorthEntryDto extends StandardDto {
    protected Long netWorthId;
    protected AssetDto assetDto;
    protected String category;
    protected BigDecimal value;
    protected Long userId;
}
