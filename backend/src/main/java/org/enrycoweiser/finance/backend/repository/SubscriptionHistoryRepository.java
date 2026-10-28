package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.SubscriptionHistory;
import org.enrycoweiser.finance.backend.repository.ext.SubscriptionHistoryRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionHistoryRepository extends StandardRepository<SubscriptionHistory>, SubscriptionHistoryRepositoryExt {

}