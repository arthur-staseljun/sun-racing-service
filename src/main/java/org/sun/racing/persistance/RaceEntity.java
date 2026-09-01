package org.sun.racing.persistance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int durationInSeconds;
}
