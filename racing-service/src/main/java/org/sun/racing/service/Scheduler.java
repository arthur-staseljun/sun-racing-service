package org.sun.racing.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.entity.RaceConsistency;
import org.sun.racing.persistance.entity.RaceEntity;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ThreadPoolTaskScheduler taskExecutor;
    private final EntityManagerFactory entityManagerFactory;

    public void runRace(UUID raceEntityId, ZonedDateTime startedAt, int durationInSeconds) {
        Runnable runRace = () -> {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            RaceEntity raceEntity = entityManager.getReference(RaceEntity.class, raceEntityId);
            raceEntity.setFinishedAt(getCurrentDateTime());
            raceEntity.setUpdatedAt(getCurrentDateTime());
            raceEntity.setRaceStatus(Race.RaceStatus.FINISHED);
            entityManager.persist(raceEntity);
            RaceConsistency raceConsistency = entityManager.getReference(RaceConsistency.class, raceEntityId);
            entityManager.remove(raceConsistency);
            entityManager.getTransaction().commit();
        };
        taskExecutor.schedule(runRace, startedAt.plus(durationInSeconds, ChronoUnit.SECONDS).toInstant());
    }
}
