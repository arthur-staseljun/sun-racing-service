package org.sun.racing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.RacesJoinedEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RacesJoinedRepository extends JpaRepository<RacesJoinedEntity, UUID> {
    Optional<RacesJoinedEntity> findByRaceIdAndParticipantId(UUID raceId, String participantId);

    void deleteAllByRaceId(UUID raceId);
}
