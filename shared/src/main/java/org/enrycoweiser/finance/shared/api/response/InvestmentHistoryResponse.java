package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.InvestmentHistoryDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class InvestmentHistoryResponse extends StandardResponse<InvestmentHistoryDto> {
    public InvestmentHistoryResponse() {
        super();
    }

    public InvestmentHistoryResponse(List<InvestmentHistoryDto> dtos) {
        super(dtos);
    }
}
