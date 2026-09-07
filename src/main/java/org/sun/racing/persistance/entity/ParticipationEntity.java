package org.sun.racing.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.sun.racing.util.Utils.getCurrentDateTime;

@Entity
@Getter
@Table(name = "participations")
@NoArgsConstructor
public class ParticipationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "race_id")
    private UUID raceId;

    @Column(name = "participant_id")
    private String participantId;

    @Setter
    private Integer score;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public ParticipationEntity(String participantId, UUID raceId) {
        this.participantId = participantId;
        this.raceId = raceId;
        this.score = 0;
        this.createdAt = getCurrentDateTime();
        this.updatedAt = getCurrentDateTime();
    }
}