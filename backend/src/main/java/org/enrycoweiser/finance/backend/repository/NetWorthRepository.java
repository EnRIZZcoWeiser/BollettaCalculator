package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.NetWorth;
import org.enrycoweiser.finance.backend.repository.ext.NetWorthRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetWorthRepository extends StandardRepository<NetWorth>, NetWorthRepositoryExt {

}