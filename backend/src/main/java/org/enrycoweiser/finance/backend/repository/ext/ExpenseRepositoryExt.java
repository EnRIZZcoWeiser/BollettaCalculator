package org.enrycoweiser.finance.backend.repository.ext;

import jakarta.persistence.TypedQuery;
import org.enrycoweiser.finance.backend.domain.Expense;
import org.enrycoweiser.finance.backend.standard.StandardEntity;
import org.enrycoweiser.finance.backend.standard.StandardRepositoryExt;

import java.util.List;
import java.util.Map;

public interface ExpenseRepositoryExt extends StandardRepositoryExt<Expense> {
}
