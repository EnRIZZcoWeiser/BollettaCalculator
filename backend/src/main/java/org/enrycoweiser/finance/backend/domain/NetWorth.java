package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.NetWorthDto;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "net_worth")
public class NetWorth extends StandardEntity {
    protected LocalDate date;
    protected Long userId;

    public NetWorthDto toDto() {
        NetWorthDto dto = new NetWorthDto();
        dto.setId(id);
        dto.setDate(date);
        dto.setUserId(userId);
        return dto;
    }
}
