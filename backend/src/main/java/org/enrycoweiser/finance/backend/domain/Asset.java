package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.AssetDto;

@Getter
@Setter
@Entity
@Table(name = "asset")
public class Asset extends StandardEntity {
    protected String name;
    protected String category;
    protected Boolean active;
    protected Long userId;

    public AssetDto toDto() {
        AssetDto dto = new AssetDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCategory(category);
        dto.setActive(active);
        dto.setUserId(userId);
        return dto;
    }

    public Asset() {

    }

    public Asset(AssetDto dto) {
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
