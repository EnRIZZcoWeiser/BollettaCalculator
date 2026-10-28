package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Investment;
import org.enrycoweiser.finance.backend.repository.ext.InvestmentRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends StandardRepository<Investment>, InvestmentRepositoryExt {

}