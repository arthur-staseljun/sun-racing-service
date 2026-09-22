package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.*;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.LeaderBoardResponse;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.model.response.RaceInfoResponse;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.RacesJoinedRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.persistance.entity.RacesJoinedEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RacingService {

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final RacesJoinedRepository racesJoinedRepository;
    private final Scheduler scheduler;

    public Race createNewRace(int duration) {
        if (duration < 1 || duration > 3600) {
            throw new RaceDurationValidationException();
        }
        RaceEntity saved = raceRepository.save(new RaceEntity(duration));
        return new Race(saved.getId(), saved.getDurationInSeconds(), saved.getRaceStatus());
    }

    @Transactional
    public ParticipationInfoResponse joinRace(UUID raceId, String participantId) {
        RaceEntity race = raceRepository.findById(raceId).orElseThrow(RaceDoesNotExist::new);
        if (race.getRaceStatus() != Race.RaceStatus.CREATED) {
            throw new RaceIsActiveOrFinished();
        }
        racesJoinedRepository.findByRaceIdAndParticipantId(raceId, participantId)
                .ifPresent(joinedRace -> {
                    throw new RaceAlreadyJoinedException();
                });
        try {
            racesJoinedRepository.save(new RacesJoinedEntity(raceId, participantId));
        } catch (DataIntegrityViolationException e) {
            throw new RaceAlreadyJoinedException();
        }

        var saved = participationRepository.save(new ParticipationEntity(participantId, race.getId()));
        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getCreatedAt());
    }

    @Transactional
    public Race startRace(UUID raceId) {
        RaceEntity race = raceRepository.findByRaceIdLocking(raceId).orElseThrow(RaceDoesNotExist::new);
        if (race.getRaceStatus() != Race.RaceStatus.CREATED) {
            throw new RaceIsActiveOrFinished();
        }
        ZonedDateTime now = getCurrentDateTime();
        race.setRaceStatus(Race.RaceStatus.ACTIVE);
        race.setStartedAt(now);
        race.setUpdatedAt(now);
        race.setShouldBeFinishedAt(now.plusSeconds(race.getDurationInSeconds()));
        RaceEntity saved = raceRepository.save(race);

        scheduler.runRace(saved.getId(), saved.getShouldBeFinishedAt());
        racesJoinedRepository.deleteAllByRaceId(raceId);
        return new Race(saved.getId(), saved.getDurationInSeconds(), saved.getRaceStatus());
    }

    @Transactional
    public boolean finishRace(UUID raceId) {
        RaceEntity raceEntity = raceRepository.findByRaceIdLocking(raceId)
                .orElseThrow(() -> new IllegalArgumentException("No race entity found with id: " + raceId));
        if (raceEntity.getRaceStatus() != Race.RaceStatus.ACTIVE) {
            log.info("Race {} has already been finished", raceId);
            return false;
        }
        ZonedDateTime now = getCurrentDateTime();
        raceEntity.setFinishedAt(now);
        raceEntity.setUpdatedAt(now);
        raceEntity.setRaceStatus(Race.RaceStatus.FINISHED);
        raceRepository.save(raceEntity);
        return true;
    }

    public RaceInfoResponse getActiveRaceInfo(UUID raceId, boolean detailed) {
        RaceEntity race = raceRepository.findById(raceId).orElseThrow(RaceDoesNotExist::new);
        if (race.getRaceStatus() != Race.RaceStatus.ACTIVE) {
            throw new RaceIsNotActiveException();
        }
        List<ParticipationEntity> participationEntities = participationRepository.getByRaceIdOrderByScoreDesc(raceId);
        return detailed ? new RaceInfoResponse(race, participationEntities) : new LeaderBoardResponse(race, participationEntities);
    }
}
