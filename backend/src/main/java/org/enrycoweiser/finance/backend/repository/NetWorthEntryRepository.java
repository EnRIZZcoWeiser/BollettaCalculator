package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.NetWorthEntry;
import org.enrycoweiser.finance.backend.repository.ext.NetWorthEntryRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetWorthEntryRepository extends StandardRepository<NetWorthEntry>, NetWorthEntryRepositoryExt {

}