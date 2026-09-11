package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.IncomeDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class IncomeResponse extends StandardResponse<IncomeDto> {
    public IncomeResponse() {
        super();
    }

    public IncomeResponse(List<IncomeDto> dtos) {
        super(dtos);
    }
}
