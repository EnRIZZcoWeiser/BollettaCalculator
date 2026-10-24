package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Income;
import org.enrycoweiser.finance.backend.repository.ext.IncomeRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class IncomeRepositoryImpl implements IncomeRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Income> retrieveByFilters(Map<String, Object> filters) {
        Object dateFrom = filters.get(FilterUtils.INCOME_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.INCOME_DATE_TO);
        Object category = filters.get(FilterUtils.INCOME_CATEGORY);
        Object paymentMethod = filters.get(FilterUtils.INCOME_PAYMENT_METHOD);

        StringBuilder hql = new StringBuilder("from Income i where 1=1 ");
        hql.append("and i.userId = :userId ");
        if(dateFrom instanceof LocalDate) {
            hql.append(" and i.date >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and i.date <= :dateTo ");
        }
        if(category instanceof String) {
            hql.append(" and i.category = :category ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and i.paymentMethod.id = :paymentMethodId ");
        }

        TypedQuery<Income> query = entityManager.createQuery(hql.toString(), Income.class);
        query.setParameter("userId", SEManager.getId());
        if(dateFrom instanceof LocalDate d) {
            query.setParameter("dateFrom", d);
        }
        if(dateTo instanceof LocalDate d) {
            query.setParameter("dateTo", d);
        }
        if(category instanceof String c) {
            query.setParameter("category", c);
        }
        if(paymentMethod instanceof PaymentMethodDto pm) {
            query.setParameter("paymentMethodId", pm.getId());
        }

        return query.getResultList();
    }

    @Override
    public List<Income> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Income i where 1=1 ");
        hql.append("and i.userId = :userId ");

        TypedQuery<Income> query = entityManager.createQuery(hql.toString(), Income.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
