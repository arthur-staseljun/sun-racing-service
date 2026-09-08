package org.sun.racing.sidewalk.donkey.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.sun.racing.sidewalk.donkey.persistance.RaceConsistencyRepository;
import org.sun.racing.sidewalk.donkey.persistance.RaceRepository;
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

    @Value("${job.race-consistency.enabled:false}")
    private Boolean raceConsistencyEnabled;

    @Value("${job.race-consistency.repeat-millis:0}")
    private int repeatMillis;

    @EventListener
    public void handleUserRegistered(ApplicationStartedEvent event) {
        this.scheduleCleanRaceStatuses();
    }


    public void scheduleCleanRaceStatuses() {
        Runnable cleanRaceStatuses = () -> {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            List<RaceConsistency> raceConsistencyEntities = raceConsistencyRepository.findAllByShouldBeFinishedAtBefore(now);
            raceConsistencyEntities.stream().forEach(raceConsistency -> {
                UUID raceId = raceConsistency.getId();
                Optional<RaceEntity> raceEntityOptional = raceRepository.findById(raceId);
                if (raceEntityOptional.isEmpty()) {
                    raceConsistencyRepository.deleteById(raceId);
                    return;
                }
                RaceEntity raceEntity = raceEntityOptional.get();
                raceEntity.setRaceStatus(RaceEntity.RaceStatus.FINISHED);
                raceEntity.setUpdatedAt(now);
                raceEntity.setFinishedAt(now);
                raceRepository.saveAndFlush(raceEntity);
                raceConsistencyRepository.delete(raceConsistency);
                raceConsistencyRepository.flush();
            });
        };
        if (raceConsistencyEnabled) {
            taskExecutor.scheduleAtFixedRate(cleanRaceStatuses, Duration.ofMillis(repeatMillis));
        }
    }
}
