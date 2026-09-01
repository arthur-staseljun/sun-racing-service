package org.sun.racing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Race {

    private final String id;
    private final int durationInSeconds;
    private final RaceStatus raceStatus;
    public enum RaceStatus {
        CREATED, ACTIVE, FINISHED
    }
}

