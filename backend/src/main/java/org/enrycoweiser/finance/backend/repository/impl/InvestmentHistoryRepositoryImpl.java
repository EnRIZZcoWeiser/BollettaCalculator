package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.InvestmentHistory;
import org.enrycoweiser.finance.backend.repository.ext.InvestmentHistoryRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class InvestmentHistoryRepositoryImpl implements InvestmentHistoryRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InvestmentHistory> retrieveByFilters(Map<String, Object> filters) {
        Object investment = filters.get(FilterUtils.INVESTMENT_HISTORY_INVESTMENT);
        Object type = filters.get(FilterUtils.INVESTMENT_HISTORY_TYPE);
        Object paymentMethod = filters.get(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD);
        Object dateFrom = filters.get(FilterUtils.INVESTMENT_HISTORY_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.INVESTMENT_HISTORY_DATE_TO);

        StringBuilder hql = new StringBuilder("from InvestmentHistory h where 1=1 ");
        hql.append("and h.userId = :userId ");
        if(investment instanceof Long) {
            hql.append(" and h.investment.id = :investmentId ");
        }
        if(type instanceof String) {
            hql.append(" and h.type = :type ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and h.paymentMethod.id = :paymentMethodId ");
        }
        if(dateFrom instanceof LocalDate) {
            hql.append(" and h.date >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and h.date <= :dateTo ");
        }
        hql.append(" order by h.date desc ");

        TypedQuery<InvestmentHistory> query = entityManager.createQuery(hql.toString(), InvestmentHistory.class);
        query.setParameter("userId", SEManager.getId());
        if(investment instanceof Long l) {
            query.setParameter("investmentId", l);
        }
        if(type instanceof String t) {
            query.setParameter("type", t);
        }
        if(paymentMethod instanceof PaymentMethodDto pm) {
            query.setParameter("paymentMethodId", pm.getId());
        }
        if(dateFrom instanceof LocalDate d) {
            query.setParameter("dateFrom", d);
        }
        if(dateTo instanceof LocalDate d) {
            query.setParameter("dateTo", d);
        }

        return query.getResultList();
    }

    @Override
    public List<InvestmentHistory> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from InvestmentHistory h where 1=1 ");
        hql.append("and h.userId = :userId ");

        TypedQuery<InvestmentHistory> query = entityManager.createQuery(hql.toString(), InvestmentHistory.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
