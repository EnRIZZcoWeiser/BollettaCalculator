package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.SubscriptionHistoryDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subscription_history")
public class SubscriptionHistory extends StandardEntity {
    @ManyToOne
    @JoinColumn(name = "id_subscription")
    protected Subscription subscription;

    protected LocalDate startDate;
    protected LocalDate endDate;
    protected BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "id_payment_method")
    protected PaymentMethod paymentMethod;

    protected Long userId;

    public SubscriptionHistoryDto toDto() {
        SubscriptionHistoryDto dto = new SubscriptionHistoryDto();
        dto.setId(id);
        if(subscription != null) {
            dto.setSubscriptionId(subscription.getId());
        } else {
            dto.setSubscriptionId(null);
        }
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setPrice(price);
        dto.setMonthlyAmount(monthlyAmount());
        if(paymentMethod != null) {
            dto.setPaymentMethodDto(paymentMethod.toDto());
        } else {
            dto.setPaymentMethodDto(null);
        }
        dto.setUserId(userId);
        return dto;
    }

    /*
        Every row is an annual period paid in advance:
        on a single month it weighs price / 12.
     */
    public BigDecimal monthlyAmount() {
        if(price == null) {
            return BigDecimal.ZERO;
        }

        return price.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    /*
        A row is active on a date if the date falls between start and end.
     */
    public boolean isActiveOn(LocalDate date) {
        if(startDate == null || endDate == null) {
            return false;
        }

        return !startDate.isAfter(date) && !endDate.isBefore(date);
    }
}
