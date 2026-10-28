package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.SubscriptionDto;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription extends StandardEntity {
    protected String name;
    protected Boolean active;
    protected Long userId;

    public SubscriptionDto toDto() {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(id);
        dto.setName(name);
        dto.setActive(active);
        dto.setUserId(userId);
        return dto;
    }

    public Subscription() {

    }

    public Subscription(SubscriptionDto dto) {
        if(dto == null) {
            return;
        }

        this.id = dto.getId();
        this.name = dto.getName();
        this.active = dto.getActive();
        this.userId = dto.getUserId();
    }
}
