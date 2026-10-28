package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;

@Getter
@Setter
@Entity
@Table(name = "investment")
public class Investment extends StandardEntity {
    protected String name;
    protected String category;
    protected Boolean active;
    protected Long userId;

    public InvestmentDto toDto() {
        InvestmentDto dto = new InvestmentDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCategory(category);
        dto.setActive(active);
        dto.setUserId(userId);
        return dto;
    }

    public Investment() {

    }

    public Investment(InvestmentDto dto) {
        if(dto == null) {
            return;
        }

        this.id = dto.getId();
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.active = dto.getActive();
        this.userId = dto.getUserId();
    }
}
