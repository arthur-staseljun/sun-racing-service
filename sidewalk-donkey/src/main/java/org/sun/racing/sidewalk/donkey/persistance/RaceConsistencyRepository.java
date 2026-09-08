package org.sun.racing.sidewalk.donkey.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.sidewalk.donkey.persistance.entity.RaceConsistency;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RaceConsistencyRepository extends JpaRepository<RaceConsistency, UUID> {
    List<RaceConsistency> findAllByShouldBeFinishedAtBefore(ZonedDateTime now);
}
