package org.sun.racing.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sun.racing.model.Race;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "race_status")
    private Race.RaceStatus raceStatus;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Setter
    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Setter
    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;

    @Setter
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public RaceEntity(int durationInSeconds, Race.RaceStatus raceStatus) {
        this.durationInSeconds = durationInSeconds;
        this.raceStatus = raceStatus;
        this.createdAt = getCurrentDateTime();
    }
}
