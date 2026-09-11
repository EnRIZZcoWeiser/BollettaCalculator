package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Expense;
import org.enrycoweiser.finance.backend.repository.ext.ExpenseRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends StandardRepository<Expense>, ExpenseRepositoryExt {

}
