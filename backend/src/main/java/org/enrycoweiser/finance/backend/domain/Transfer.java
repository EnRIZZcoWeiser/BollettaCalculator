package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.TransferDto;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
    Money moved from one payment method to another (ATM withdrawal, top-up...).
    It is neither an expense nor an income: it only changes the liquidity of the two payment methods.
 */
@Getter
@Setter
@Entity
@Table(name = "transfer")
public class Transfer extends StandardEntity {
    protected LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_payment_method_from")
    protected PaymentMethod fromPaymentMethod;

    @ManyToOne
    @JoinColumn(name = "id_payment_method_to")
    protected PaymentMethod toPaymentMethod;

    protected BigDecimal money;
    protected String note;
    protected Long userId;

    public TransferDto toDto() {
        TransferDto dto = new TransferDto();
        dto.setId(id);
        dto.setDate(date);
        if(fromPaymentMethod != null) {
            dto.setFromPaymentMethodDto(fromPaymentMethod.toDto());
        } else {
            dto.setFromPaymentMethodDto(null);
        }
        if(toPaymentMethod != null) {
            dto.setToPaymentMethodDto(toPaymentMethod.toDto());
        } else {
            dto.setToPaymentMethodDto(null);
        }
        dto.setMoney(money);
        dto.setNote(note);
        dto.setUserId(userId);
        return dto;
    }
}
