package org.enrycoweiser.finance.backend.standard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandardRepository<E extends StandardEntity> extends JpaRepository<E, Long> {
    default List<E> retrieveAll() {
        return findAll();
    }

    default Optional<E> retrieveById(Long id) {
        if(id == null) {
            return Optional.empty();
        }
        return findById(id);
    }

    default void saveEntity(E entity) {
        save(entity);
    }

    default void deleteEntity(E entity) {
        delete(entity);
    }
}
