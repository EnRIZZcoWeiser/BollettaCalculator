package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.enrycoweiser.finance.backend.domain.Income;
import org.enrycoweiser.finance.backend.repository.ext.IncomeRepositoryExt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class IncomeRepositoryImpl implements IncomeRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Income> retrieveByFilters(Map<String, Object> filters) {
        return List.of();
    }

    public List<Income> retrieveByUser(Long id) {
        return List.of();
    }
}
