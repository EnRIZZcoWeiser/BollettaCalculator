package org.enrycoweiser.finance.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.ExpenseDto;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Expense extends StandardEntity {
    protected Date date;
    protected BigDecimal money;
    protected String location;
    protected String store;
    protected String category;
    protected PaymentMethod paymentMethod;
    protected String note;
    protected Long userId;

    public ExpenseDto toDto() {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(id);
        dto.setDate(date);
        dto.setMoney(money);
        dto.setLocation(location);
        dto.setStore(store);
        dto.setCategory(category);
        if(paymentMethod != null) {
            dto.setPaymentMethod(paymentMethod.toDto());
        } else {
            dto.setPaymentMethod(null);
        }
        dto.setNote(note);
        dto.setUserId(userId);
        return dto;
    }
}
