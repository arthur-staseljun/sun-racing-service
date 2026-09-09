package org.sun.racing.service;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.sun.racing.events.EntityUnfreezeEvent;
import org.sun.racing.events.RaceFinishEvent;
import org.sun.racing.persistance.entity.ParticipationEntity;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ThreadPoolTaskScheduler taskExecutor;
    private final ApplicationEventPublisher eventPublisher;

    public void runRace(UUID raceEntityId, int durationInSeconds) {
        Runnable finishRace = () -> eventPublisher.publishEvent(new RaceFinishEvent(raceEntityId));
        ZonedDateTime raceFinishDateTime = getCurrentDateTime().plus(Duration.ofSeconds(durationInSeconds));
        log.info("Scheduling race {} finish event at {}", raceEntityId, raceFinishDateTime);
        taskExecutor.schedule(finishRace, raceFinishDateTime.toInstant());
    }

    public void unfreeze(ParticipationEntity freezedEntity, long freezeDurationInMilliseconds) {
        Runnable unfreeze = () -> eventPublisher.publishEvent(new EntityUnfreezeEvent(freezedEntity.getId()));
        ZonedDateTime unfreezeDateTime = getCurrentDateTime().plus(Duration.ofMillis(freezeDurationInMilliseconds));
        log.info("Scheduling unfreezing participationId {} at {}", freezedEntity.getId(), unfreezeDateTime);
        taskExecutor.schedule(unfreeze, unfreezeDateTime.toInstant());

    }
}
