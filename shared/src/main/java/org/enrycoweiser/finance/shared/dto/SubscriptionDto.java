package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionDto extends StandardDto {
    protected String name;
    protected Boolean active;
    protected Long userId;

    /* calculated from the current history row */
    protected BigDecimal price;
    protected BigDecimal monthlyAmount;

    @Override
    public String toString() {
        return this.name;
    }
}
