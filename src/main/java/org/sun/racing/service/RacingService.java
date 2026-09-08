package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.ParticipantAlreadyJoinedException;
import org.sun.racing.exception.RaceDoesNotExist;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.exception.RaceIsActiveOrFinished;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.model.response.RaceInfoResponse;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.util.List;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Service
@RequiredArgsConstructor
public class RacingService {

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final Scheduler scheduler;

    public Race createNewRace(int duration) {
        if (duration < 1 || duration > 3600) {
            throw new RaceDurationValidationException();
        }
        RaceEntity raceEntity = new RaceEntity(duration, Race.RaceStatus.CREATED);
        RaceEntity saved = raceRepository.save(raceEntity);
        return new Race(saved.getId(), saved.getDurationInSeconds(), saved.getRaceStatus());
    }

    public ParticipationInfoResponse joinRace(UUID raceId, String participantId) {
        var optionalRace = raceRepository.findById(raceId);
        if (optionalRace.isEmpty()) {
            throw new RaceDoesNotExist();
        }
        RaceEntity race = optionalRace.get();
        if (!Race.RaceStatus.CREATED.equals(race.getRaceStatus())) {
            throw new RaceIsActiveOrFinished();
        }

        var optionalParticipation = participationRepository.getByRaceIdAndParticipantId(race.getId(), participantId);
        if (optionalParticipation.isPresent()) {
            throw new ParticipantAlreadyJoinedException();
        }

        ParticipationEntity participationEntity = new ParticipationEntity(participantId, race.getId());
        var saved = participationRepository.save(participationEntity);
        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getCreatedAt());
    }

    @Transactional
    public Race startRace(UUID raceId) {
        var optionalRace = raceRepository.findByRaceIdLocking(raceId);
        if (optionalRace.isEmpty()) {
            throw new RaceDoesNotExist();
        }
        RaceEntity race = optionalRace.get();
        if (!Race.RaceStatus.CREATED.equals(race.getRaceStatus())) {
            throw new RaceIsActiveOrFinished();
        }
        race.setRaceStatus(Race.RaceStatus.ACTIVE);
        race.setStartedAt(getCurrentDateTime());
        race.setUpdatedAt(getCurrentDateTime());
        RaceEntity saved = raceRepository.save(race);

        scheduler.runRace(saved.getId(), saved.getStartedAt(), saved.getDurationInSeconds());
        return new Race(saved.getId(), saved.getDurationInSeconds(), saved.getRaceStatus());
    }

    public RaceInfoResponse getRaceInfo(UUID raceId) {
        var optionalRace = raceRepository.findById(raceId);
        if (optionalRace.isEmpty()) {
            throw new RaceDoesNotExist();
        }
        RaceEntity race = optionalRace.get();
        List<ParticipationEntity> participationEntities = participationRepository.getByRaceId(raceId);
        return new RaceInfoResponse(race, participationEntities);
    }
}
