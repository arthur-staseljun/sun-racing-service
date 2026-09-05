package org.sun.racing.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.sun.racing.model.Race;

import java.util.UUID;

@Entity
@Getter
@Table(name = "races")
public class RaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "duration_in_seconds")
    private int durationInSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "race_status")
    private Race.RaceStatus raceStatus;

    public RaceEntity(int durationInSeconds, Race.RaceStatus raceStatus) {
        this.durationInSeconds = durationInSeconds;
        this.raceStatus = raceStatus;
    }
}
