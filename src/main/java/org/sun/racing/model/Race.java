package org.sun.racing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Race {

    private final int durationInSeconds;

    public enum RaceStatus {
        PENDING, STARTED,  FINISHED
    }
}

