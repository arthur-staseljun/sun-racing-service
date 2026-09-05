package org.sun.racing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.RaceEntity;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, Long> {
}
