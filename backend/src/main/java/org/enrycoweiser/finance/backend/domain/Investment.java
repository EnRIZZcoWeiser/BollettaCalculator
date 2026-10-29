package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;

/*
    Name and category come from the asset (the same master data used by the net worth).
    An asset can be linked to one investment only, and never a LIQUIDITY one.
 */
@Getter
@Setter
@Entity
@Table(name = "investment")
public class Investment extends StandardEntity {
    @ManyToOne
    @JoinColumn(name = "id_asset")
    protected Asset asset;

    protected Boolean active;
    protected Long userId;

    public InvestmentDto toDto() {
        InvestmentDto dto = new InvestmentDto();
        dto.setId(id);
        if(asset != null) {
            dto.setAssetDto(asset.toDto());
            dto.setName(asset.getName());
            dto.setCategory(asset.getCategory());
        } else {
            dto.setAssetDto(null);
            dto.setName(null);
            dto.setCategory(null);
        }
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
        this.asset = new Asset(dto.getAssetDto());
        this.active = dto.getActive();
        this.userId = dto.getUserId();
    }
}
