package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.NetWorthEntryDto;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "net_worth_entry")
public class NetWorthEntry extends StandardEntity {
    @ManyToOne
    @JoinColumn(name = "id_net_worth")
    protected NetWorth netWorth;

    @ManyToOne
    @JoinColumn(name = "id_asset")
    protected Asset asset;

    protected BigDecimal value;
    protected Long userId;

    public NetWorthEntryDto toDto() {
        NetWorthEntryDto dto = new NetWorthEntryDto();
        dto.setId(id);
        if(netWorth != null) {
            dto.setNetWorthId(netWorth.getId());
        } else {
            dto.setNetWorthId(null);
        }
        if(asset != null) {
            dto.setAssetDto(asset.toDto());
            dto.setCategory(asset.getCategory());
        } else {
            dto.setAssetDto(null);
            dto.setCategory(null);
        }
        dto.setValue(value);
        dto.setUserId(userId);
        return dto;
    }
}
