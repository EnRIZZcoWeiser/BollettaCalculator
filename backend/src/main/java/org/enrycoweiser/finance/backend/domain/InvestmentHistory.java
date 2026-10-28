package org.enrycoweiser.finance.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.shared.dto.InvestmentHistoryDto;
import org.enrycoweiser.finance.shared.utils.StaticUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/*
    A single buy or sell of an investment.
    BUY:  amount + fee leave the payment method
    SELL: amount - fee enter the payment method
 */
@Getter
@Setter
@Entity
@Table(name = "investment_history")
public class InvestmentHistory extends StandardEntity {
    @ManyToOne
    @JoinColumn(name = "id_investment")
    protected Investment investment;

    protected LocalDate date;
    protected String type;
    protected BigDecimal amount;
    protected BigDecimal fee;

    // the default BigDecimal column keeps only 2 decimals, shares need more
    @Column(precision = 20, scale = 8)
    protected BigDecimal shares;

    @ManyToOne
    @JoinColumn(name = "id_payment_method")
    protected PaymentMethod paymentMethod;

    protected String note;
    protected Long userId;

    public InvestmentHistoryDto toDto() {
        InvestmentHistoryDto dto = new InvestmentHistoryDto();
        dto.setId(id);
        if(investment != null) {
            dto.setInvestmentId(investment.getId());
        } else {
            dto.setInvestmentId(null);
        }
        dto.setDate(date);
        dto.setType(type);
        dto.setAmount(amount);
        dto.setFee(fee);
        dto.setShares(shares);
        if(paymentMethod != null) {
            dto.setPaymentMethodDto(paymentMethod.toDto());
        } else {
            dto.setPaymentMethodDto(null);
        }
        dto.setNote(note);
        dto.setUserId(userId);
        dto.setPricePerShare(pricePerShare());
        return dto;
    }

    public boolean isBuy() {
        return StaticUtils.INVESTMENT_BUY.equals(type);
    }

    public boolean isSell() {
        return StaticUtils.INVESTMENT_SELL.equals(type);
    }

    public BigDecimal pricePerShare() {
        if(amount == null || shares == null || shares.signum() == 0) {
            return null;
        }

        return amount.divide(shares, 4, RoundingMode.HALF_UP);
    }
}
