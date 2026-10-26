package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.NetWorthEntryDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class NetWorthEntryResponse extends StandardResponse<NetWorthEntryDto> {
    public NetWorthEntryResponse() {
        super();
    }

    public NetWorthEntryResponse(List<NetWorthEntryDto> dtos) {
        super(dtos);
    }
}
