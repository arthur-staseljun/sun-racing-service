package org.sun.racing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.ParticipationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    Optional<ParticipationEntity> getByRaceIdAndParticipantId(UUID raceId, String participantId);

    List<ParticipationEntity> getByRaceId(UUID raceId);

    List<ParticipationEntity> getByRaceIdOrderByScoreDesc(UUID raceId);
}
