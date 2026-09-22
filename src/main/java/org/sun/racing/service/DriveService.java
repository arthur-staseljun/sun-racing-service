package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sun.racing.exception.ParticipantIsNotParticipatingInRace;
import org.sun.racing.exception.RaceDoesNotExist;
import org.sun.racing.exception.RaceIsNotActiveException;
import org.sun.racing.model.Race;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.util.UUID;
import java.util.function.IntSupplier;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriveService {

    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    public ParticipationInfoResponse drive(UUID raceId, String participantId, IntSupplier scoreSupplier) {
        getActiveRace(raceId);
        ParticipationEntity participation = participationRepository.getByRaceIdAndParticipantId(raceId, participantId)
                        .orElseThrow(ParticipantIsNotParticipatingInRace::new);
        if (participation.isFreezed()) {
            return new ParticipationInfoResponse(participation);
        }
        participationRepository.updateScore(participation.getId(), scoreSupplier.getAsInt(), getCurrentDateTime());
        return new ParticipationInfoResponse(participationRepository.findById(participation.getId()).orElseThrow());
    }

    public RaceEntity getActiveRace(UUID raceId) {
        RaceEntity race = raceRepository.findById(raceId).orElseThrow(RaceDoesNotExist::new);
        if (!Race.RaceStatus.ACTIVE.equals(race.getRaceStatus())) {
            throw new RaceIsNotActiveException();
        }
        return race;
    }
}
