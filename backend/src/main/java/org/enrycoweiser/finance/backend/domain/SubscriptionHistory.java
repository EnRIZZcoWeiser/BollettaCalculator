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
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

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
        dto.setMonths(months());
        if(paymentMethod != null) {
            dto.setPaymentMethodDto(paymentMethod.toDto());
        } else {
            dto.setPaymentMethodDto(null);
        }
        dto.setUserId(userId);
        return dto;
    }

    /*
        Months covered by the period, start and end month included:
        10/2026 - 09/2027 = 12, 10/2026 - 03/2028 = 18.
     */
    public int months() {
        if(startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return 0;
        }

        return (int) ChronoUnit.MONTHS.between(YearMonth.from(startDate), YearMonth.from(endDate)) + 1;
    }

    /*
        The price is paid in advance and spread over the months of the period:
        on a single month it weighs price / months.
     */
    public BigDecimal monthlyAmount() {
        int months = months();
        if(price == null || months == 0) {
            return BigDecimal.ZERO;
        }

        return price.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
    }

    /*
        Only the month counts: the start is stored as the first day of its month,
        the end as the last day of its month.
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date != null ? YearMonth.from(date).atDay(1) : null;
    }

    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date != null ? YearMonth.from(date).atEndOfMonth() : null;
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
