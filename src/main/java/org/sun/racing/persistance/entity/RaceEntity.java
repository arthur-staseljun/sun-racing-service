package org.sun.racing.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sun.racing.model.Race;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "races")
@NoArgsConstructor
public class RaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "duration_in_seconds")
    private int durationInSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "race_status")
    private Race.RaceStatus raceStatus;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;

    public RaceEntity(int durationInSeconds, Race.RaceStatus raceStatus) {
        this.durationInSeconds = durationInSeconds;
        this.raceStatus = raceStatus;
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    }
}
