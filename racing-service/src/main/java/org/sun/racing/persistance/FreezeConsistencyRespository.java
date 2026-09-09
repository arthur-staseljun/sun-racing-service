package org.sun.racing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.FreezeConsistencyEntity;

@Repository
public interface FreezeConsistencyRespository extends JpaRepository<FreezeConsistencyEntity, Long> {
}
