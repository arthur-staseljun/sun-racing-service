package org.sun.racing.sidewalk.donkey.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.sun.racing.sidewalk.donkey.persistance.entity.FreezeConsistencyEntity;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface FreezeConsistencyRespository extends JpaRepository<FreezeConsistencyEntity, Long> {

    @Query("select fc from FreezeConsistencyEntity fc where fc.shouldBeUnfreezedAt <= :now")
    List<FreezeConsistencyEntity> findAllWithShouldBeUnfreezedAtOrBefore(@Param("now") ZonedDateTime now);
}
