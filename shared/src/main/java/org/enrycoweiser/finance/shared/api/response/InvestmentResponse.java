package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;

import java.util.List;

public class InvestmentResponse extends StandardResponse<InvestmentDto> {
    public InvestmentResponse() {
        super();
    }

    public InvestmentResponse(List<InvestmentDto> dtos) {
        super(dtos);
    }
}
