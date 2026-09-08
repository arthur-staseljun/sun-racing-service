package org.sun.racing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.entity.RaceEntity;
import org.sun.racing.persistance.RaceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RacingServiceTest {
    @Mock
    private RaceRepository raceRepository;
    @Mock
    private ParticipationRepository participationRepository;
    @Mock
    private Scheduler scheduler;

    private RacingService racingService;

    @BeforeEach
    void setUp() {
        racingService = new RacingService(raceRepository, participationRepository, scheduler);
    }

    @Test
    void createRaceWithMinDuration() {
        when(raceRepository.save(any(RaceEntity.class))).thenReturn(new RaceEntity(1, Race.RaceStatus.CREATED));
        Race newRace = racingService.createNewRace(1);
        assertEquals(Race.RaceStatus.CREATED, newRace.getRaceStatus());
        assertEquals(1, newRace.getDurationInSeconds());
    }

    @Test
    void createRaceWithMaxDuration() {
        when(raceRepository.save(any(RaceEntity.class))).thenReturn(new RaceEntity(3600, Race.RaceStatus.CREATED));
        Race newRace = racingService.createNewRace(3600);
        assertEquals(Race.RaceStatus.CREATED, newRace.getRaceStatus());
        assertEquals(3600, newRace.getDurationInSeconds());
    }

    @Test
    void testInvalidRaceDuration() {
        assertThrows(RaceDurationValidationException.class, () -> racingService.createNewRace(0));
        assertThrows(RaceDurationValidationException.class, () -> racingService.createNewRace(-1));
        assertThrows(RaceDurationValidationException.class, () -> racingService.createNewRace(3601));
    }
}