package org.sun.racing.sidewalk.donkey.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.sun.racing.sidewalk.donkey.persistance.FreezeConsistencyRespository;
import org.sun.racing.sidewalk.donkey.persistance.ParticipationRepository;
import org.sun.racing.sidewalk.donkey.persistance.RaceConsistencyRepository;
import org.sun.racing.sidewalk.donkey.persistance.RaceRepository;
import org.sun.racing.sidewalk.donkey.persistance.entity.FreezeConsistencyEntity;
import org.sun.racing.sidewalk.donkey.persistance.entity.ParticipationEntity;
import org.sun.racing.sidewalk.donkey.persistance.entity.RaceConsistency;
import org.sun.racing.sidewalk.donkey.persistance.entity.RaceEntity;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ThreadPoolTaskScheduler taskExecutor;
    private final RaceConsistencyRepository raceConsistencyRepository;
    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final FreezeConsistencyRespository freezeConsistencyRespository;

    @Value("${job.race-consistency.repeat-millis:0}")
    private int repeatMillis;

    @EventListener
    public void handleUserRegistered(ApplicationStartedEvent event) {
        this.scheduleTasks();
    }

    public void scheduleTasks() {
        Runnable cleanRaceStatuses = () -> {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            List<RaceConsistency> raceConsistencyEntities = raceConsistencyRepository.findAllWithShouldBeFinishedAtOrBefore(now);
            raceConsistencyEntities.forEach(raceConsistency -> {UUID raceId = raceConsistency.getRaceId();
                Optional<RaceEntity> raceEntityOptional = raceRepository.findById(raceId);
                if (raceEntityOptional.isEmpty()) {
                    log.error("Race not found with id {}", raceId);
                    raceConsistencyRepository.deleteById(raceId);
                    return;
                }
                RaceEntity raceEntity = raceEntityOptional.get();
                raceEntity.setRaceStatus(RaceEntity.RaceStatus.FINISHED);
                raceEntity.setUpdatedAt(now);
                raceEntity.setFinishedAt(now);
                raceRepository.save(raceEntity);
                log.info("Race {} finished", raceId);
                raceConsistencyRepository.delete(raceConsistency);
                log.info("Race consistency entity {} has been deleted", raceConsistency.getRaceId());
            });
        };
        Runnable unfreezeParticipants = () -> {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            List<FreezeConsistencyEntity> unfreezedEntities =
                    freezeConsistencyRespository.findAllWithShouldBeUnfreezedAtOrBefore(now);
            unfreezedEntities.forEach(unfreezeParticipant -> {
                Long unfreezeParticipantId = unfreezeParticipant.getParticipationId();
                var unfreezedPartitipationOptional = participationRepository.findById(unfreezeParticipantId);
                if (unfreezedPartitipationOptional.isEmpty()) {
                    log.error("Participation not found with id {}", unfreezeParticipantId);
                    freezeConsistencyRespository.deleteById(unfreezeParticipantId);
                    return;
                }
                ParticipationEntity unfreezedPartitipation = unfreezedPartitipationOptional.get();
                unfreezedPartitipation.setFreezed(false);
                unfreezedPartitipation.setUpdatedAt(now);
                participationRepository.save(unfreezedPartitipation);
                log.info("Participation {} has been unfreezed", unfreezeParticipantId);
                freezeConsistencyRespository.delete(unfreezeParticipant);
                log.info("Freeze participation {} has been deleted", unfreezeParticipantId);
            });
        };
        taskExecutor.scheduleAtFixedRate(cleanRaceStatuses, Duration.ofMillis(repeatMillis));
        taskExecutor.scheduleAtFixedRate(unfreezeParticipants, Duration.ofMillis(repeatMillis));
    }
}
