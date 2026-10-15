package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.ext.PaymentMethodRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PaymentMethodRepositoryImpl implements PaymentMethodRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentMethod> retrieveByFilters(Map<String, Object> filters) {
        Object name = filters.get(FilterUtils.PAYMENT_METHOD_NAME);
        Object onlyActive = filters.get(FilterUtils.PAYMENT_METHOD_ONLY_ACTIVE);

        StringBuilder hql = new StringBuilder("from PaymentMethod p where 1=1 ");
        hql.append("and p.userId = :userId ");
        if(name instanceof String) {
            hql.append(" and p.name like :name ");
        }
        if(onlyActive instanceof Boolean b && b) {
            hql.append(" and p.active is true ");
        }

        TypedQuery<PaymentMethod> query = entityManager.createQuery(hql.toString(), PaymentMethod.class);
        query.setParameter("userId", SEManager.getId());
        if(name instanceof String s) {
            query.setParameter("name", "%" + s + "%");
        }

        return query.getResultList();
    }

    public List<PaymentMethod> retrieveByUser(Long id) {
        return List.of();
    }
}
