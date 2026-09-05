package org.sun.racing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Race {

    private final UUID id;
    private final int durationInSeconds;
    private final RaceStatus raceStatus;
    public enum RaceStatus {
        CREATED, ACTIVE, FINISHED
    }
}

