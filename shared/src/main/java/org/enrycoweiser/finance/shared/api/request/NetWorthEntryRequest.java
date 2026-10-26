package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.math.BigDecimal;

@Getter
@Setter
public class NetWorthEntryRequest extends StandardRequest {
    protected Long netWorthId;
    protected AssetDto asset;
    protected String category;
    protected BigDecimal value;
    protected Long userId;
}
