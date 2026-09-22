package org.sun.racing.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.service.RacingService;
import org.sun.racing.service.ReportingService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceApplicationEventListener {

    private final ParticipationRepository participationRepository;
    private final RacingService racingService;
    private final ReportingService reportingService;

    @EventListener
    @Transactional
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
        participationRepository.unfreeze(participationEntity.getId(), getCurrentDateTime());
    }

    @EventListener
    public void finishRace(RaceFinishEvent event) {
        UUID raceId = event.getRaceEntityId();
        boolean isFinishedNow = racingService.finishRace(raceId);
        if (isFinishedNow) {
            reportingService.reportWinners(raceId);
            return;
        }
        log.info("Race {} already finished, skipping reporting",  raceId);
    }
}
