package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

@Getter
@Setter
public class InvestmentRequest extends StandardRequest {
    protected AssetDto asset;
    protected Boolean active;
    protected Long userId;

    /* filter only: name and category of the asset */
    protected String name;
    protected String category;
}
