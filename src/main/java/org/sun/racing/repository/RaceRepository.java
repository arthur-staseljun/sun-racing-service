package org.sun.racing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.RaceEntity;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, Long> {
}
