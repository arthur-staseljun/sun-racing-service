package org.sun.racing.persistance;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.ParticipationEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    Optional<ParticipationEntity> getByRaceIdAndParticipantId(UUID raceId, String participantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Set<ParticipationEntity> getAllByRaceIdOrderByIdAsc(UUID raceId);

    @Modifying(clearAutomatically = true)
    @Query("""
              update ParticipationEntity p set updatedAt = CURRENT_TIMESTAMP, score = 
                case 
                    when score + :delta > 0 then score + :delta
                    else 0 
                end
              where p.id = :participationId
            """)
    int updateScore(@Param("participationId") long participationId, @Param("delta") int delta);

    List<ParticipationEntity> getByRaceIdOrderByScoreDesc(UUID raceId);

    @Modifying(clearAutomatically = true)
    @Query(value = """
              update participations p 
                set freezed = true, updated_at = CURRENT_TIMESTAMP, should_be_unfreezed_at = case
                    when p.should_be_unfreezed_at IS NULL then CURRENT_TIMESTAMP + (:freezeDurationInMilliseconds * interval '1 Millisecond')
                    else p.should_be_unfreezed_at + (:freezeDurationInMilliseconds * interval '1 Millisecond')
                end
              where p.id = :participationId
           """, nativeQuery = true)
    int freezeOrExtend(long participationId, long freezeDurationInMilliseconds);


    @Modifying(clearAutomatically = true)
    @Query("""
                update ParticipationEntity p 
                    set freezed = false, updatedAt = CURRENT_TIMESTAMP, shouldBeUnfreezedAt = NULL
                where p.id = :participationId and p.freezed = true and p.shouldBeUnfreezedAt <= CURRENT_TIMESTAMP
           """)
    int unfreeze(long participationId);

    List<ParticipationEntity> getTop3ByRaceIdOrderByScoreDesc(UUID raceId);

    @Query("select entity from ParticipationEntity entity where entity.freezed = TRUE " +
            "and entity.shouldBeUnfreezedAt <= CURRENT_TIMESTAMP")
    List<ParticipationEntity> findAllUnfreezeOverdue();
}
