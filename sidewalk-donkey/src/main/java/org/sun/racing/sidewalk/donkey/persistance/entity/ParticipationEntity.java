package org.sun.racing.sidewalk.donkey.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "participations")
@NoArgsConstructor
public class ParticipationEntity {

    @Id
    private Long id;

    @Version
    private Long version;

    @Column(name = "race_id")
    private UUID raceId;

    @Column(name = "participant_id")
    private String participantId;

    private Integer score;

    @Setter
    private boolean freezed;

    @Setter
    @Column(name = "should_be_unfreezed_at")
    private ZonedDateTime shouldBeUnfreezedAt;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Setter
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}