package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.sun.racing.events.EntityUnfreezeEvent;
import org.sun.racing.events.RaceApplicationEventListener;
import org.sun.racing.events.RaceFinishEvent;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ThreadPoolTaskScheduler taskExecutor;
    private final ApplicationEventPublisher eventPublisher;
    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;

    public void runRace(UUID raceEntityId, ZonedDateTime shouldBeFinishedAt) {
        Runnable finishRace = () -> eventPublisher.publishEvent(new RaceFinishEvent(raceEntityId));
        log.info("Scheduling race {} finish event at {}", raceEntityId, getCurrentDateTime());
        taskExecutor.schedule(finishRace, shouldBeFinishedAt.toInstant());
    }

    public void unfreeze(ParticipationEntity freezedEntity) {
        Runnable unfreeze = () -> eventPublisher.publishEvent(new EntityUnfreezeEvent(freezedEntity.getId()));
        ZonedDateTime unfreezeDateTime = freezedEntity.getShouldBeUnfreezedAt();
        log.info("Scheduling unfreezing participationId {} at {}", freezedEntity.getId(), unfreezeDateTime);
        taskExecutor.schedule(unfreeze, unfreezeDateTime.toInstant());
    }

    @Scheduled(fixedRate = 5000)
    public void finishOverdueRaces() {
        List<RaceEntity> overdueRaces = raceRepository.findAllOverdue();
        overdueRaces.forEach(race -> eventPublisher.publishEvent(new RaceFinishEvent(race.getId())));
    }

    @Scheduled(fixedRate = 5000)
    public void unfreezeOverdueEntities() {
        List<ParticipationEntity> overdueRaces = participationRepository.findAllUnfreezeOverdue();
        overdueRaces.forEach(participationEntity ->
                eventPublisher.publishEvent(new EntityUnfreezeEvent(participationEntity.getId())));
    }
}
