package org.sun.racing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.model.Race;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
}
