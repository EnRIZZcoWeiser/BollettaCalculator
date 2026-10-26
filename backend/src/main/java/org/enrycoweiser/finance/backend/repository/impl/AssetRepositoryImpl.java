package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.repository.ext.AssetRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AssetRepositoryImpl implements AssetRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Asset> retrieveByFilters(Map<String, Object> filters) {
        Object name = filters.get(FilterUtils.ASSET_NAME);
        Object category = filters.get(FilterUtils.ASSET_CATEGORY);
        Object onlyActive = filters.get(FilterUtils.ASSET_ONLY_ACTIVE);

        StringBuilder hql = new StringBuilder("from Asset a where 1=1 ");
        hql.append("and a.userId = :userId ");
        if(name instanceof String) {
            hql.append(" and a.name like :name ");
        }
        if(category instanceof String) {
            hql.append(" and a.category = :category ");
        }
        if(onlyActive instanceof Boolean b && b) {
            hql.append(" and a.active is true ");
        }
        hql.append(" order by a.category, a.name ");

        TypedQuery<Asset> query = entityManager.createQuery(hql.toString(), Asset.class);
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
    public List<Asset> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Asset a where 1=1 ");
        hql.append("and a.userId = :userId ");
        hql.append(" order by a.category, a.name ");

        TypedQuery<Asset> query = entityManager.createQuery(hql.toString(), Asset.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
