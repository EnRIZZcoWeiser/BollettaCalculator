package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.ext.PaymentMethodRepositoryExt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PaymentMethodRepositoryImpl implements PaymentMethodRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentMethod> retrieveByFilters(Map<String, Object> filters) {
        return List.of();
    }
}
