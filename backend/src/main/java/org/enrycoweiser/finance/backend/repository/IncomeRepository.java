package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Income;
import org.enrycoweiser.finance.backend.repository.ext.IncomeRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends StandardRepository<Income>, IncomeRepositoryExt {

}