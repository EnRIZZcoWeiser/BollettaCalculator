package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.DashboardDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class DashboardResponse extends StandardResponse<DashboardDto> {
    public DashboardResponse() {
        super();
    }

    public DashboardResponse(List<DashboardDto> dtos) {
        super(dtos);
    }
}
