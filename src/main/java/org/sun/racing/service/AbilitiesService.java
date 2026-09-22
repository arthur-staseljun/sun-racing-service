package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.ParticipantHasFewPoints;
import org.sun.racing.exception.ParticipantIsNotParticipatingInRace;
import org.sun.racing.exception.RaceDoesNotExist;
import org.sun.racing.exception.RaceIsNotActiveException;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbilitiesService {
    private static final int OIL_SLICK_COST = 10;
    private static final int ENGINE_HACK_COST = 20;
    private static final int MAX_ENGINE_HACK_LOOSE_POINTS = 10;

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final Scheduler scheduler;
    private final Random randomInstance = new Random();

    @Transactional
    public ParticipationInfoResponse slickOil(UUID raceId, String participantId) {
        RaceEntity race = getActiveRaceEntity(raceId);
        ParticipationEntity initiator = participationRepository.getByRaceIdAndParticipantId(race.getId(), participantId)
                .orElseThrow(ParticipantIsNotParticipatingInRace::new);

        Set<ParticipationEntity> participationEntities = participationRepository.getAllByRaceIdOrderByIdAsc(raceId);
        for (ParticipationEntity participation : participationEntities) {
            if (participation.getId().equals(initiator.getId())) {
                int updatedScore = participation.getScore() - OIL_SLICK_COST;
                if (updatedScore < 0) {
                    throw new ParticipantHasFewPoints();
                }
                participationRepository.updateScore(participation.getId(), -OIL_SLICK_COST);
            } else {
                long freezeDurationInMilliseconds = getFreezeDurationInMilliseconds(race);
                log.info("Freezing participant with id: {} for {} milliseconds", participation.getParticipantId(), freezeDurationInMilliseconds);
                participationRepository.freezeOrExtend(participation.getId(), freezeDurationInMilliseconds);
                ParticipationEntity refreshed = participationRepository.getByRaceIdAndParticipantId(race.getId(), participation.getParticipantId()).orElseThrow();
                scheduler.unfreeze(refreshed);
            }
        }
        return new ParticipationInfoResponse(participationRepository.findById(initiator.getId()).orElseThrow());
    }

    private static long getFreezeDurationInMilliseconds(RaceEntity race) {
        return BigDecimal.valueOf(race.getDurationInSeconds())
                .scaleByPowerOfTen(-2)
                .multiply(BigDecimal.valueOf(1000))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
    }

    @Transactional
    public ParticipationInfoResponse hackEngine(UUID raceId, String participantId) {
        RaceEntity race = getActiveRaceEntity(raceId);
        ParticipationEntity initiator = participationRepository.getByRaceIdAndParticipantId(race.getId(), participantId)
                .orElseThrow(ParticipantIsNotParticipatingInRace::new);

        int hackPoints = randomInstance.nextInt(MAX_ENGINE_HACK_LOOSE_POINTS) + 1;
        Set<ParticipationEntity> participationEntities = participationRepository.getAllByRaceIdOrderByIdAsc(raceId);
        for (ParticipationEntity participation : participationEntities) {
            if (participation.getId().equals(initiator.getId())) {
                int updatedScore = participation.getScore() - ENGINE_HACK_COST;
                if (updatedScore < 0) {
                    throw new ParticipantHasFewPoints();
                }
                participationRepository.updateScore(participation.getId(), -ENGINE_HACK_COST);
            } else {
                log.info("Hacking participant with id: {}, he looses {} points",
                        participation.getId(), hackPoints);
                participationRepository.updateScore(participation.getId(), -hackPoints);
            }
        }
        return new ParticipationInfoResponse(participationRepository.findById(initiator.getId()).orElseThrow());
    }

    private RaceEntity getActiveRaceEntity(UUID raceId) {
        RaceEntity race = raceRepository.findById(raceId).orElseThrow(RaceDoesNotExist::new);
        if (!Race.RaceStatus.ACTIVE.equals(race.getRaceStatus())) {
            throw new RaceIsNotActiveException();
        }
        return race;
    }
}
