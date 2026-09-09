package org.sun.racing.sidewalk.donkey.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sun.racing.sidewalk.donkey.persistance.entity.ParticipationEntity;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {
}
