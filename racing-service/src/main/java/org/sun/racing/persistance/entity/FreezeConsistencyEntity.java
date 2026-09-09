package org.sun.racing.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "freeze_consistency")
public class FreezeConsistencyEntity {
    @Id
    @Column(name = "participation_id")
    private Long participationId;

    @Column(name = "freezed_at")
    private ZonedDateTime freezedAt;

    @Setter
    @Column(name = "should_be_unfreezed_at")
    private ZonedDateTime shouldBeUnfreezedAt;
}
