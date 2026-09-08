package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.ParticipantHasFewPoints;
import org.sun.racing.exception.ParticipantIsNotParticipatingInRace;
import org.sun.racing.exception.RaceDoesNotExist;
import org.sun.racing.exception.RaceIsNotActive;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbilitiesService {
    private static final int OIL_SLICK_COST = 10;

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final Scheduler scheduler;

    public synchronized ParticipationInfoResponse slickOil(UUID raceId, String participantId) {
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
        initiator.setScore(updatedScore);
        initiator.setUpdatedAt(Utils.getCurrentDateTime());
        ParticipationEntity saved = participationRepository.save(initiator);

        Set<ParticipationEntity> participationEntities = Set.copyOf(participationRepository.getByRaceId(raceId));
        for (ParticipationEntity freezedEntity : participationEntities) {
            if (!freezedEntity.getId().equals(initiator.getId())) {
                long freezeDurationInMilliseconds = BigDecimal.valueOf(race.getDurationInSeconds())
                        .scaleByPowerOfTen(-2)
                        .multiply(BigDecimal.valueOf(1000))
                        .setScale(0, RoundingMode.HALF_UP)
                        .longValue();
                log.info("Freezing participant with id: {} for {} milliseconds", freezedEntity.getId(), freezeDurationInMilliseconds);
                freezedEntity.setFreezed(true);
                freezedEntity.setUpdatedAt(Utils.getCurrentDateTime());
                participationRepository.save(freezedEntity);
                scheduler.unfreeze(freezedEntity.getId(), freezedEntity.getUpdatedAt(), freezeDurationInMilliseconds);
            }
        }

        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getScore(), saved.isFreezed(),
                saved.getUpdatedAt(), saved.getCreatedAt());
    }
}
