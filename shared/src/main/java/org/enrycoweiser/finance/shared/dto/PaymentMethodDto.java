package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

@Getter
@Setter
public class PaymentMethodDto extends StandardDto {
    protected String name;
    protected Boolean active;
    protected Long userId;
}
