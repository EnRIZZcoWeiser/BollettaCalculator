package org.enrycoweiser.finance.backend.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;

@Getter
@Setter
public class PaymentMethod extends StandardEntity {
    protected String name;
    protected Boolean active;
    protected Long userId;

    public PaymentMethodDto toDto() {
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(id);
        dto.setName(name);
        dto.setActive(active);
        dto.setUserId(userId);
        return dto;
    }

    /*public PaymentMethod() {
        id = null;
        name = null;
        active = null;
        userId = null;
    }*/

    public PaymentMethod(PaymentMethodDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.active = dto.getActive();
        this.userId = dto.getUserId();
    }
}
