package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardRequest;

import java.time.LocalDate;

@Getter
@Setter
public class NetWorthRequest extends StandardRequest {
    protected LocalDate date;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected Long userId;
}
