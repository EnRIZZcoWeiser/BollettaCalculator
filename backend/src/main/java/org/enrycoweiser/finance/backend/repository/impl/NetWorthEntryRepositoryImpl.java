package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.NetWorthEntry;
import org.enrycoweiser.finance.backend.repository.ext.NetWorthEntryRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NetWorthEntryRepositoryImpl implements NetWorthEntryRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NetWorthEntry> retrieveByFilters(Map<String, Object> filters) {
        Object netWorth = filters.get(FilterUtils.NET_WORTH_ENTRY_NET_WORTH);
        Object asset = filters.get(FilterUtils.NET_WORTH_ENTRY_ASSET);
        Object category = filters.get(FilterUtils.NET_WORTH_ENTRY_CATEGORY);

        StringBuilder hql = new StringBuilder("from NetWorthEntry e where 1=1 ");
        hql.append("and e.userId = :userId ");
        if(netWorth instanceof Long) {
            hql.append(" and e.netWorth.id = :netWorthId ");
        }
        if(asset instanceof AssetDto) {
            hql.append(" and e.asset.id = :assetId ");
        }
        if(category instanceof String) {
            hql.append(" and e.asset.category = :category ");
        }
        hql.append(" order by e.asset.category, e.asset.name ");

        TypedQuery<NetWorthEntry> query = entityManager.createQuery(hql.toString(), NetWorthEntry.class);
        query.setParameter("userId", SEManager.getId());
        if(netWorth instanceof Long l) {
            query.setParameter("netWorthId", l);
        }
        if(asset instanceof AssetDto a) {
            query.setParameter("assetId", a.getId());
        }
        if(category instanceof String c) {
            query.setParameter("category", c);
        }

        return query.getResultList();
    }

    @Override
    public List<NetWorthEntry> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from NetWorthEntry e where 1=1 ");
        hql.append("and e.userId = :userId ");

        TypedQuery<NetWorthEntry> query = entityManager.createQuery(hql.toString(), NetWorthEntry.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
