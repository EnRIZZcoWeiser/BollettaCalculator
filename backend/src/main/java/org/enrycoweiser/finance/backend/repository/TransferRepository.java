package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Transfer;
import org.enrycoweiser.finance.backend.repository.ext.TransferRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends StandardRepository<Transfer>, TransferRepositoryExt {

}