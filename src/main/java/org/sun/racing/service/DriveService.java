package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
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

import java.util.UUID;

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

        int score = engine.getScore();
        int updatedScore = participation.getScore() + score;
        participation.setScore(updatedScore);

        var saved = participationRepository.save(participation);
        return new ParticipationInfoResponse(
                saved.getRaceId(), saved.getParticipantId(), saved.getScore(),
                saved.getUpdatedAt(), saved.getCreatedAt());
    }

}
