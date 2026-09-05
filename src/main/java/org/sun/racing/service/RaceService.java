package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.persistance.RaceRepository;

@Service
@RequiredArgsConstructor
public class RaceService {

    private final RaceRepository raceRepository;

    @Transactional
    public Race createNewRace(int duration) {
        if (duration < 1 || duration > 3600) {
            throw new RaceDurationValidationException();
        }
        RaceEntity raceEntity = new RaceEntity(duration, Race.RaceStatus.CREATED);
        RaceEntity saved = raceRepository.save(raceEntity);
        return new Race(saved.getId(), saved.getDurationInSeconds(), saved.getRaceStatus());
    }
}
