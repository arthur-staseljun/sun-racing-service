package org.sun.racing.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.persistance.entity.RaceConsistency;

import java.util.UUID;

@Repository
public interface RaceConsistencyRepository extends JpaRepository<RaceConsistency, UUID> {
}
