package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Subscription;
import org.enrycoweiser.finance.backend.repository.ext.SubscriptionRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends StandardRepository<Subscription>, SubscriptionRepositoryExt {

}