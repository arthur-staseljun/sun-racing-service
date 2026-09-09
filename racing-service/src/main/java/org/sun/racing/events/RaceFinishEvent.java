package org.sun.racing.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class RaceFinishEvent extends ApplicationEvent {
    @Getter
    private final UUID raceEntityId;
    public RaceFinishEvent(UUID raceEntityId) {
        super(raceEntityId);
        this.raceEntityId = raceEntityId;
    }
}
