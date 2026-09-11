package org.enrycoweiser.finance.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.IncomeDto;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Income extends StandardEntity {
    private Date date;
    private BigDecimal money;
    private String category;
    private PaymentMethod paymentMethod;
    private String note;
    private Long userId;

    public IncomeDto toDto() {
        IncomeDto dto = new IncomeDto();
        dto.setId(id);
        dto.setMoney(money);
        dto.setCategory(category);
        if(paymentMethod != null) {
            dto.setPaymentMethodDto(paymentMethod.toDto());
        } else {
            dto.setPaymentMethodDto(null);
        }
        dto.setNote(note);
        dto.setUserId(userId);
        return dto;
    }
}
