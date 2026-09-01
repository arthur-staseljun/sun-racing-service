package org.sun.racing.persistance;

import jakarta.persistence.*;
import lombok.Getter;
import org.sun.racing.model.Race;

@Entity
@Getter
public class RaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int durationInSeconds;

    @Enumerated(EnumType.STRING)
    private Race.RaceStatus raceStatus;

    public RaceEntity(int durationInSeconds, Race.RaceStatus raceStatus) {
        this.durationInSeconds = durationInSeconds;
        this.raceStatus = raceStatus;
    }
}
