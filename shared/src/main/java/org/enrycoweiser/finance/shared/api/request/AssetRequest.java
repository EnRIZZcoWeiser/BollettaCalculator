package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

@Getter
@Setter
public class AssetRequest extends StandardRequest {
    protected String name;
    protected String category;
    protected Boolean active;
    protected Long userId;
}
