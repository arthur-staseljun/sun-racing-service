package org.sun.racing.persistance;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.RaceEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT race FROM RaceEntity race WHERE race.id = :raceId")
    Optional<RaceEntity> findByRaceIdLocking(UUID raceId);
    Optional<RaceEntity> findById(UUID raceId);
}
