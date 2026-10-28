package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.InvestmentHistory;
import org.enrycoweiser.finance.backend.repository.ext.InvestmentHistoryRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentHistoryRepository extends StandardRepository<InvestmentHistory>, InvestmentHistoryRepositoryExt {

}