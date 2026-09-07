package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.RaceEntity;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Component
@RequiredArgsConstructor
public class RaceExecutor {

    private final RaceRepository raceRepository;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void runRace(UUID raceId, int durationInSeconds) {
        Runnable race = () -> {
            RaceEntity raceEntity = raceRepository.findById(raceId)
                    .orElseThrow(() -> new IllegalArgumentException("Race with id: " + raceId + " not found"));
            raceEntity.setFinishedAt(getCurrentDateTime());
            raceEntity.setUpdatedAt(getCurrentDateTime());
            raceEntity.setRaceStatus(Race.RaceStatus.FINISHED);
            raceRepository.save(raceEntity);
        };
        schedule(race, durationInSeconds, TimeUnit.SECONDS);
    }

    private void schedule(Runnable command, long delay, TimeUnit unit) {
        scheduler.schedule(() -> executor.execute(command), delay, unit);
    }
}
