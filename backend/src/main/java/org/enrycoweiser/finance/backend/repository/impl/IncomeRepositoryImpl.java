package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Income;
import org.enrycoweiser.finance.backend.repository.ext.IncomeRepositoryExt;
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
        Object date_from = filters.get(FilterUtils.INCOME_DATE_FROM);
        Object date_to = filters.get(FilterUtils.INCOME_DATE_TO);
        Object money = filters.get(FilterUtils.INCOME_MONEY);
        Object category = filters.get(FilterUtils.INCOME_CATEGORY);
        Object paymentMethod = filters.get(FilterUtils.INCOME_PAYMENT_METHOD);

        StringBuilder hql = new StringBuilder("from Income i where 1=1 ");
        hql.append("and i.userId = :userId ");
        if(date_from instanceof LocalDate) {
            hql.append(" and i.date >= :date ");
        }
        if(date_to instanceof LocalDate) {
            hql.append(" and i.date <= :date ");
        }
        if(money instanceof Double) {
            hql.append(" and i.money >= :money ");
        }
        if(category instanceof String) {
            hql.append(" and i.category = :category ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and i.paymentMethod = :paymentMethod ");
        }

        TypedQuery<Income> query = entityManager.createQuery(hql.toString(), Income.class);
        if(date_from instanceof LocalDate d) {
            query.setParameter("date_from", d);
        }
        if(date_to instanceof LocalDate d) {
            query.setParameter("date_to", d);
        }
        if(money instanceof Double m) {
            query.setParameter("money", m);
        }
        if(category instanceof String c) {
            query.setParameter("category", c);
        }
        if(paymentMethod instanceof PaymentMethodDto pm) {
            query.setParameter("paymentMethod", pm);
        }

        return query.getResultList();
    }

    public List<Income> retrieveByUser(Long id) {
        return List.of();
    }
}
