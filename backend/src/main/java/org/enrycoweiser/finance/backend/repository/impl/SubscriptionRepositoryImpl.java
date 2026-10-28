package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Subscription;
import org.enrycoweiser.finance.backend.repository.ext.SubscriptionRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Subscription> retrieveByFilters(Map<String, Object> filters) {
        Object name = filters.get(FilterUtils.SUBSCRIPTION_NAME);
        Object onlyActive = filters.get(FilterUtils.SUBSCRIPTION_ONLY_ACTIVE);

        StringBuilder hql = new StringBuilder("from Subscription s where 1=1 ");
        hql.append("and s.userId = :userId ");
        if(name instanceof String) {
            hql.append(" and s.name like :name ");
        }
        if(onlyActive instanceof Boolean b && b) {
            hql.append(" and s.active is true ");
        }

        TypedQuery<Subscription> query = entityManager.createQuery(hql.toString(), Subscription.class);
        query.setParameter("userId", SEManager.getId());
        if(name instanceof String s) {
            query.setParameter("name", "%" + s + "%");
        }

        return query.getResultList();
    }

    @Override
    public List<Subscription> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Subscription s where 1=1 ");
        hql.append("and s.userId = :userId ");

        TypedQuery<Subscription> query = entityManager.createQuery(hql.toString(), Subscription.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
