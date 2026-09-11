package org.enrycoweiser.finance.shared.api.response;

import org.enrycoweiser.finance.shared.dto.ExpenseDto;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import java.util.List;

public class ExpenseResponse extends StandardResponse<ExpenseDto> {
    public ExpenseResponse() {
        super();
    }

    public ExpenseResponse(List<ExpenseDto> dtos) {
        super(dtos);
    }
}
