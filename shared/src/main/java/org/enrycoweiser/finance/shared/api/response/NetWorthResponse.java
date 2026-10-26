package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.NetWorthDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class NetWorthResponse extends StandardResponse<NetWorthDto> {
    public NetWorthResponse() {
        super();
    }

    public NetWorthResponse(List<NetWorthDto> dtos) {
        super(dtos);
    }
}
