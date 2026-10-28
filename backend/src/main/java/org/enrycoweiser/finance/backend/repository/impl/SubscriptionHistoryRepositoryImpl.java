package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.SubscriptionHistory;
import org.enrycoweiser.finance.backend.repository.ext.SubscriptionHistoryRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class SubscriptionHistoryRepositoryImpl implements SubscriptionHistoryRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    /*
        DATE_FROM / DATE_TO select the rows that overlap the period.
     */
    @Override
    public List<SubscriptionHistory> retrieveByFilters(Map<String, Object> filters) {
        Object subscription = filters.get(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION);
        Object paymentMethod = filters.get(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD);
        Object dateFrom = filters.get(FilterUtils.SUBSCRIPTION_HISTORY_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.SUBSCRIPTION_HISTORY_DATE_TO);

        StringBuilder hql = new StringBuilder("from SubscriptionHistory h where 1=1 ");
        hql.append("and h.userId = :userId ");
        if(subscription instanceof Long) {
            hql.append(" and h.subscription.id = :subscriptionId ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and h.paymentMethod.id = :paymentMethodId ");
        }
        if(dateFrom instanceof LocalDate) {
            hql.append(" and h.endDate >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and h.startDate <= :dateTo ");
        }
        hql.append(" order by h.startDate desc ");

        TypedQuery<SubscriptionHistory> query = entityManager.createQuery(hql.toString(), SubscriptionHistory.class);
        query.setParameter("userId", SEManager.getId());
        if(subscription instanceof Long l) {
            query.setParameter("subscriptionId", l);
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
    public List<SubscriptionHistory> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from SubscriptionHistory h where 1=1 ");
        hql.append("and h.userId = :userId ");

        TypedQuery<SubscriptionHistory> query = entityManager.createQuery(hql.toString(), SubscriptionHistory.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
