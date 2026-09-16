package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.ext.PaymentMethodRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends StandardRepository<PaymentMethod>, PaymentMethodRepositoryExt {

}