package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sun.racing.exception.ParticipantHasFewPoints;
import org.sun.racing.exception.ParticipantIsNotParticipatingInRace;
import org.sun.racing.exception.RaceDoesNotExist;
import org.sun.racing.exception.RaceIsNotActive;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.persistance.FreezeConsistencyRespository;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.FreezeConsistencyEntity;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbilitiesService {
    private static final int OIL_SLICK_COST = 10;
    private static final int ENGINE_HACK_COST = 20;
    private static final int MAX_ENGINE_HACK_LOOSE_POINTS = 10;

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final FreezeConsistencyRespository freezeConsistencyRespository;

    private Random randomInstance = new Random();

    public ParticipationInfoResponse slickOil(UUID raceId, String participantId) {
        var optionalRace = raceRepository.findById(raceId);
        if (optionalRace.isEmpty()) {
            throw new RaceDoesNotExist();
        }
        RaceEntity race = optionalRace.get();
        if (!Race.RaceStatus.ACTIVE.equals(race.getRaceStatus())) {
            throw new RaceIsNotActive();
        }

        var optionalParticipation = participationRepository.getByRaceIdAndParticipantId(race.getId(), participantId);
        if (optionalParticipation.isEmpty()) {
            throw new ParticipantIsNotParticipatingInRace();
        }
        ParticipationEntity initiator = optionalParticipation.get();

        int updatedScore = initiator.getScore() - OIL_SLICK_COST;
        if (updatedScore < 0) {
            throw new ParticipantHasFewPoints();
        }
        ParticipationEntity saved = freeze(raceId, initiator, updatedScore, race);

        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getScore(), saved.isFreezed(),
                saved.getUpdatedAt(), saved.getCreatedAt());
    }

    private synchronized ParticipationEntity freeze(UUID raceId, ParticipationEntity initiator,
                                                    int updatedScore, RaceEntity race) {
        initiator.setScore(updatedScore);
        ZonedDateTime now = getCurrentDateTime();
        initiator.setUpdatedAt(now);
        ParticipationEntity saved = participationRepository.save(initiator);

        Set<ParticipationEntity> participationEntities = Set.copyOf(participationRepository.getByRaceId(raceId));
        for (ParticipationEntity freezedEntity : participationEntities) {
            if (!freezedEntity.getId().equals(initiator.getId())) {
                long freezeDurationInMilliseconds = BigDecimal.valueOf(race.getDurationInSeconds())
                        .scaleByPowerOfTen(-2)
                        .multiply(BigDecimal.valueOf(1000))
                        .setScale(0, RoundingMode.HALF_UP)
                        .longValue();
                log.info("Freezing participant with id: {} for {} milliseconds", freezedEntity.getParticipantId(), freezeDurationInMilliseconds);
                freezedEntity.setFreezed(true);
                freezedEntity.setUpdatedAt(now);
                participationRepository.save(freezedEntity);
                var freezeConsistencyEntityOptional = freezeConsistencyRespository.findById(freezedEntity.getId());
                ZonedDateTime updatedShouldBeUnfreezedAt;
                FreezeConsistencyEntity freezeConsistency;
                if (freezeConsistencyEntityOptional.isEmpty()) {
                    updatedShouldBeUnfreezedAt = now.plus(Duration.ofMillis(freezeDurationInMilliseconds));
                    freezeConsistency = new FreezeConsistencyEntity(freezedEntity.getId(), now, updatedShouldBeUnfreezedAt);
                    log.info("Creating freezed consitency entity with should_be_unfreezed_at: {}", updatedShouldBeUnfreezedAt);
                } else {
                    freezeConsistency =  freezeConsistencyEntityOptional.get();
                    ZonedDateTime shouldBeUnfreezedAt = freezeConsistency.getShouldBeUnfreezedAt();
                    updatedShouldBeUnfreezedAt = shouldBeUnfreezedAt.plus(Duration.ofMillis(freezeDurationInMilliseconds));
                    log.info("Adding {} milliseconds to freezed consitency old value: {}, new value: {}",
                            freezeDurationInMilliseconds, shouldBeUnfreezedAt, updatedShouldBeUnfreezedAt);
                }
                freezeConsistency.setShouldBeUnfreezedAt(updatedShouldBeUnfreezedAt);
                freezeConsistencyRespository.save(freezeConsistency);
            }
        }
        return saved;
    }

    public ParticipationInfoResponse hackEngine(UUID raceId, String participantId) {
        var optionalRace = raceRepository.findById(raceId);
        if (optionalRace.isEmpty()) {
            throw new RaceDoesNotExist();
        }
        RaceEntity race = optionalRace.get();
        if (!Race.RaceStatus.ACTIVE.equals(race.getRaceStatus())) {
            throw new RaceIsNotActive();
        }

        var optionalParticipation = participationRepository.getByRaceIdAndParticipantId(race.getId(), participantId);
        if (optionalParticipation.isEmpty()) {
            throw new ParticipantIsNotParticipatingInRace();
        }
        ParticipationEntity initiator = optionalParticipation.get();

        int updatedScore = initiator.getScore() - ENGINE_HACK_COST;
        if (updatedScore < 0) {
            throw new ParticipantHasFewPoints();
        }
        ParticipationEntity saved = hackEngine(raceId, initiator, updatedScore);

        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getScore(), saved.isFreezed(),
                saved.getUpdatedAt(), saved.getCreatedAt());
    }

    private synchronized ParticipationEntity hackEngine(UUID raceId, ParticipationEntity initiator, int updatedScore) {
        initiator.setScore(updatedScore);
        initiator.setUpdatedAt(getCurrentDateTime());
        ParticipationEntity saved = participationRepository.save(initiator);

        int hackPoints = randomInstance.nextInt(MAX_ENGINE_HACK_LOOSE_POINTS) + 1;
        Set<ParticipationEntity> participationEntities = Set.copyOf(participationRepository.getByRaceId(raceId));
        for (ParticipationEntity hackedEntity : participationEntities) {
            if (!hackedEntity.getId().equals(initiator.getId())) {
                int hackedScore = hackedEntity.getScore() - hackPoints;
                int resultingScore = hackedScore < 0 ? 0 : hackedScore;
                log.info("Hacking participant with id: {}, he looses {} points, resulting score: {}",
                        hackedEntity.getId(), hackPoints, resultingScore);
                hackedEntity.setScore(resultingScore);
                hackedEntity.setUpdatedAt(getCurrentDateTime());
                participationRepository.save(hackedEntity);
            }
        }
        return saved;
    }
}
