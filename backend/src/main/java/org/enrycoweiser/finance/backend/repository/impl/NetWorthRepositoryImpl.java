package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.NetWorth;
import org.enrycoweiser.finance.backend.repository.ext.NetWorthRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class NetWorthRepositoryImpl implements NetWorthRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NetWorth> retrieveByFilters(Map<String, Object> filters) {
        Object id = filters.get(FilterUtils.ID);
        Object dateFrom = filters.get(FilterUtils.NET_WORTH_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.NET_WORTH_DATE_TO);

        StringBuilder hql = new StringBuilder("from NetWorth n where 1=1 ");
        hql.append("and n.userId = :userId ");
        if(id instanceof Long) {
            hql.append(" and n.id = :id ");
        }
        if(dateFrom instanceof LocalDate) {
            hql.append(" and n.date >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and n.date <= :dateTo ");
        }
        hql.append(" order by n.date desc ");

        TypedQuery<NetWorth> query = entityManager.createQuery(hql.toString(), NetWorth.class);
        query.setParameter("userId", SEManager.getId());
        if(id instanceof Long l) {
            query.setParameter("id", l);
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
    public List<NetWorth> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from NetWorth n where 1=1 ");
        hql.append("and n.userId = :userId ");
        hql.append(" order by n.date desc ");

        TypedQuery<NetWorth> query = entityManager.createQuery(hql.toString(), NetWorth.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
