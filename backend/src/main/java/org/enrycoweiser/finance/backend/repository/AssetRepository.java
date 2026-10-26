package org.enrycoweiser.finance.backend.repository;

import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.repository.ext.AssetRepositoryExt;
import org.enrycoweiser.finance.backend.standard.StandardRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends StandardRepository<Asset>, AssetRepositoryExt {

}