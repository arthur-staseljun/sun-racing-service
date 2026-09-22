package org.sun.racing.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "races_joined")
@NoArgsConstructor
public class RacesJoinedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "race_id")
    private UUID raceId;

    @Column(name = "participant_id")
    private String participantId;

    public RacesJoinedEntity(UUID raceId, String participantId) {
        this.raceId = raceId;
        this.participantId = participantId;
    }
}
