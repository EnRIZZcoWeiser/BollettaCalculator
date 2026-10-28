package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

@Getter
@Setter
public class DashboardRequest extends StandardRequest {
    protected Integer year;
    protected Integer month;
}
