package org.sun.racing.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceApplicationEventListener {

    private final ParticipationRepository participationRepository;
    private final RaceRepository raceRepository;

    @EventListener
    public void unfreezeEntity(EntityUnfreezeEvent event) {
        Long participationEntityId = event.getParticipationEntityId();
        Optional<ParticipationEntity> participationEntityOptional = participationRepository.findById(participationEntityId);
        if (participationEntityOptional.isEmpty()) {
            log.error("No participation entity found by race {} with id {}", participationEntityId);
            return;
        }
        ParticipationEntity participationEntity = participationEntityOptional.get();

        ZonedDateTime now = getCurrentDateTime();
        if (participationEntity.isFreezed() && participationEntity.getShouldBeUnfreezedAt().isAfter(now)) {
            log.info("Participation entity {} has been freezed repeatedly and should be unfreezed at {}, skipping",
                    participationEntityId, participationEntity.getShouldBeUnfreezedAt());
            return;
        }
        log.info("Unfreezing participation: {}", participationEntityId);
        participationEntity.setFreezed(false);
        participationEntity.setShouldBeUnfreezedAt(null);
        participationEntity.setUpdatedAt(now);
        participationRepository.save(participationEntity);
    }

    @EventListener
    public void finishRace(RaceFinishEvent event) {
        UUID raceEntityId = event.getRaceEntityId();
        Optional<RaceEntity> raceEntityOptional = raceRepository.findById(raceEntityId);
        if (raceEntityOptional.isEmpty()) {
            log.error("No race entity found with id {}", raceEntityId);
            return;
        }
        RaceEntity raceEntity = raceEntityOptional.get();

        ZonedDateTime now = getCurrentDateTime();
        raceEntity.setFinishedAt(now);
        raceEntity.setUpdatedAt(now);
        raceRepository.save(raceEntity);
        log.info("Finished race {} at {}", raceEntityId, now);
    }
}
