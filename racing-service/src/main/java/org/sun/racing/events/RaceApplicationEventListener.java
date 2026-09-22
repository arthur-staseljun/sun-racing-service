package org.sun.racing.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.RacesJoinedRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.service.ReportingService;

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
    private final ReportingService reportingService;

    @EventListener
    public void unfreezeEntity(EntityUnfreezeEvent event) {
        Long participationEntityId = event.getParticipationEntityId();
        ParticipationEntity participationEntity = participationRepository.findById(participationEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Participation " + participationEntityId + " was not found"));

        ZonedDateTime now = getCurrentDateTime();
        if (participationEntity.isFreezed() && participationEntity.getShouldBeUnfreezedAt().isAfter(now)) {
            log.info("Participation entity {} has been freezed repeatedly and should be unfreezed at {}, skipping",
                    participationEntityId, participationEntity.getShouldBeUnfreezedAt());
            return;
        }
        log.info("Unfreezing participation: {}", participationEntityId);
        participationRepository.unfreeze(participationEntity.getId());
    }

    @EventListener
    @Order(1)
    @Transactional
    public void finishRace(RaceFinishEvent event) {
        UUID raceId = event.getRaceEntityId();
        RaceEntity raceEntity = raceRepository.findByRaceIdLocking(raceId)
                .orElseThrow(() -> new IllegalArgumentException("No race entity found with id: " + raceId));
        if (raceEntity.getRaceStatus() != Race.RaceStatus.ACTIVE) {
            log.info("Race {} has already been finished", raceId);
            return;
        }
        ZonedDateTime now = getCurrentDateTime();
        raceEntity.setFinishedAt(now);
        raceEntity.setUpdatedAt(now);
        raceEntity.setRaceStatus(Race.RaceStatus.FINISHED);
        raceRepository.save(raceEntity);
        log.info("Finished race {} at {}", raceId, now);
    }

    @EventListener
    @Order(2)
    public void reportWinners(RaceFinishEvent event) {
        UUID raceId = event.getRaceEntityId();
        reportingService.reportWinners(raceId);
    }
}
