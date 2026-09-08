package org.sun.racing.sidewalk.donkey.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "race_status")
    private RaceStatus raceStatus;

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

    public enum RaceStatus {
        CREATED("created"),
        ACTIVE("active"),
        FINISHED("finished");
        private String status;
        RaceStatus(String staus) {
            this.status = staus;
        }
    }
}
