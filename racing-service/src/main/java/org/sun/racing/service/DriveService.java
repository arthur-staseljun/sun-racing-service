package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriveService {

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;
    private final Engine engine;

    @Transactional
    public ParticipationInfoResponse drive(UUID raceId, String participantId) {
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
        ParticipationEntity participation = optionalParticipation.get();
        if (participation.isFreezed()) {
            log.info("Participation {} is freezed", participation.getId());
            return new ParticipationInfoResponse(
                    participation.getRaceId(), participation.getParticipantId(), participation.getScore(), participation.isFreezed(),
                    participation.getUpdatedAt(), participation.getCreatedAt());
        }

        int score = engine.getScore();
        int updatedScore = participation.getScore() + score;
        participation.setScore(updatedScore);
        participation.setUpdatedAt(Utils.getCurrentDateTime());

        var saved = participationRepository.save(participation);
        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getScore(), saved.isFreezed(),
                saved.getUpdatedAt(), saved.getCreatedAt());
    }

}
