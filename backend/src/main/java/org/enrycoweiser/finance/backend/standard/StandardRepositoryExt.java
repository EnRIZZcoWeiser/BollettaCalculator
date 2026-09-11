package org.enrycoweiser.finance.backend.standard;

import java.util.List;
import java.util.Map;

public interface StandardRepositoryExt<E extends StandardEntity> {
    List<E> retrieveByFilters(Map<String, Object> filters);
}
