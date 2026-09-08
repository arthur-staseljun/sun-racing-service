package org.sun.racing.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "race_consistency")
public class RaceConsistency {
    @Id
    private UUID id;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "should_be_finished_at")
    private ZonedDateTime shouldBeFinishedAt;
}
