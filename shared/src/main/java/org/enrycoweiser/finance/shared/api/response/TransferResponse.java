package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.TransferDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class TransferResponse extends StandardResponse<TransferDto> {
    public TransferResponse() {
        super();
    }

    public TransferResponse(List<TransferDto> dtos) {
        super(dtos);
    }
}
