package org.enrycoweiser.finance.backend.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Transfer;
import org.enrycoweiser.finance.backend.repository.ext.TransferRepositoryExt;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class TransferRepositoryImpl implements TransferRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    /*
        FROM / TO filter on one side of the transfer,
        PAYMENT_METHOD matches the transfers where the payment method is on either side.
     */
    @Override
    public List<Transfer> retrieveByFilters(Map<String, Object> filters) {
        Object dateFrom = filters.get(FilterUtils.TRANSFER_DATE_FROM);
        Object dateTo = filters.get(FilterUtils.TRANSFER_DATE_TO);
        Object from = filters.get(FilterUtils.TRANSFER_FROM);
        Object to = filters.get(FilterUtils.TRANSFER_TO);
        Object paymentMethod = filters.get(FilterUtils.TRANSFER_PAYMENT_METHOD);

        StringBuilder hql = new StringBuilder("from Transfer t where 1=1 ");
        hql.append("and t.userId = :userId ");
        if(dateFrom instanceof LocalDate) {
            hql.append(" and t.date >= :dateFrom ");
        }
        if(dateTo instanceof LocalDate) {
            hql.append(" and t.date <= :dateTo ");
        }
        if(from instanceof PaymentMethodDto) {
            hql.append(" and t.fromPaymentMethod.id = :fromId ");
        }
        if(to instanceof PaymentMethodDto) {
            hql.append(" and t.toPaymentMethod.id = :toId ");
        }
        if(paymentMethod instanceof PaymentMethodDto) {
            hql.append(" and (t.fromPaymentMethod.id = :paymentMethodId or t.toPaymentMethod.id = :paymentMethodId) ");
        }
        hql.append(" order by t.date desc ");

        TypedQuery<Transfer> query = entityManager.createQuery(hql.toString(), Transfer.class);
        query.setParameter("userId", SEManager.getId());
        if(dateFrom instanceof LocalDate d) {
            query.setParameter("dateFrom", d);
        }
        if(dateTo instanceof LocalDate d) {
            query.setParameter("dateTo", d);
        }
        if(from instanceof PaymentMethodDto pm) {
            query.setParameter("fromId", pm.getId());
        }
        if(to instanceof PaymentMethodDto pm) {
            query.setParameter("toId", pm.getId());
        }
        if(paymentMethod instanceof PaymentMethodDto pm) {
            query.setParameter("paymentMethodId", pm.getId());
        }

        return query.getResultList();
    }

    @Override
    public List<Transfer> retrieveByUser(Long id) {
        StringBuilder hql = new StringBuilder("from Transfer t where 1=1 ");
        hql.append("and t.userId = :userId ");

        TypedQuery<Transfer> query = entityManager.createQuery(hql.toString(), Transfer.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
}
