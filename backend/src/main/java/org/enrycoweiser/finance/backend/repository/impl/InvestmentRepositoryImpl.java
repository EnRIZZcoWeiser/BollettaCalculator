package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Investment;
import org.enrycoweiser.finance.backend.repository.ext.InvestmentRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InvestmentRepositoryImpl implements InvestmentRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Investment> retrieveByFilters(Map<String, Object> filters) {
        Object name = filters.get(FilterUtils.INVESTMENT_NAME);
        Object category = filters.get(FilterUtils.INVESTMENT_CATEGORY);
        Object onlyActive = filters.get(FilterUtils.INVESTMENT_ONLY_ACTIVE);

        StringBuilder hql = new StringBuilder("from Investment i where 1=1 ");
        hql.append("and i.userId = :userId ");
        if(name instanceof String) {
            hql.append(" and i.name like :name ");
        }
        if(category instanceof String) {
            hql.append(" and i.category = :category ");
        }
        if(onlyActive instanceof Boolean b && b) {
            hql.append(" and i.active is true ");
        }
        hql.append(" order by i.category, i.name ");

        TypedQuery<Investment> query = entityManager.createQuery(hql.toString(), Investment.class);
        query.setParameter("userId", SEManager.getId());
        if(name instanceof String s) {
            query.setParameter("name", "%" + s + "%");
        }
        if(category instanceof String c) {
            query.setParameter("category", c);
        }

        return query.getResultList();
    }

    @Override
    public List<Investment> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Investment i where 1=1 ");
        hql.append("and i.userId = :userId ");
        hql.append(" order by i.category, i.name ");

        TypedQuery<Investment> query = entityManager.createQuery(hql.toString(), Investment.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
