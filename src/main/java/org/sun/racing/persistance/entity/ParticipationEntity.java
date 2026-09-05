package org.sun.racing.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "participations")
@NoArgsConstructor
public class ParticipationEntity {

    @Id
    @Column(name = "race_id")
    private UUID raceId;

    @Column(name = "participant_id")
    private String participantId;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    public ParticipationEntity(String participantId, UUID raceId) {
        this.participantId = participantId;
        this.raceId = raceId;
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    }
}