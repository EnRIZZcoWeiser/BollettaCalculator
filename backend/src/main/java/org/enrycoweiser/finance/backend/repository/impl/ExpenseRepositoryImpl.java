package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Expense;
import org.enrycoweiser.finance.backend.repository.ext.ExpenseRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Expense> retrieveByFilters(Map<String, Object> filters) {
        Object dateFrom = filters.get(FilterUtils.EXPENSE_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.EXPENSE_DATE_TO);
        Object location = filters.get(FilterUtils.EXPENSE_LOCATION);
        Object store = filters.get(FilterUtils.EXPENSE_STORE);
        Object category = filters.get(FilterUtils.EXPENSE_CATEGORY);
        Object paymentMethod = filters.get(FilterUtils.EXPENSE_PAYMENT_METHOD);

        StringBuilder hql = new StringBuilder("from Expense e where 1=1 ");
        hql.append("and e.userId = :userId ");
        if(dateFrom instanceof LocalDate) {
            hql.append(" and e.date >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and e.date <= :dateTo ");
        }
        if(location instanceof String) {
            hql.append(" and e.location like :location ");
        }
        if(store instanceof String) {
            hql.append(" and e.store like :store ");
        }
        if(category instanceof String) {
            hql.append(" and e.category = :category ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and e.paymentMethod.id = :paymentMethodId ");
        }

        TypedQuery<Expense> query = entityManager.createQuery(hql.toString(), Expense.class);
        query.setParameter("userId", SEManager.getId());
        if(dateFrom instanceof LocalDate d) {
            query.setParameter("dateFrom", d);
        }
        if(dateTo instanceof LocalDate d) {
            query.setParameter("dateTo", d);
        }
        if(location instanceof String l) {
            query.setParameter("location", "%" + l + "%");
        }
        if(store instanceof String s) {
            query.setParameter("store", "%" + s + "%");
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
    public List<Expense> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Expense e where 1=1 ");
        hql.append("and e.userId = :userId ");

        TypedQuery<Expense> query = entityManager.createQuery(hql.toString(), Expense.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
