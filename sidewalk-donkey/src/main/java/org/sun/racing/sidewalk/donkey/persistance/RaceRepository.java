package org.sun.racing.sidewalk.donkey.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.sidewalk.donkey.persistance.entity.RaceEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, UUID> {
    Optional<RaceEntity> findById(UUID raceId);
}
