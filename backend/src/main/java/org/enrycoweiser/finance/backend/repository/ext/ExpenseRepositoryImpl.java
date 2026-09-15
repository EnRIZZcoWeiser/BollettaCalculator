package org.enrycoweiser.finance.backend.repository.ext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.enrycoweiser.finance.backend.domain.Expense;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Expense> retrieveByFilters(Map<String, Object> filters) {
        return List.of();
    }

    public List<Expense> retrieveByUser(Long id) {
        return List.of();
    }
}
