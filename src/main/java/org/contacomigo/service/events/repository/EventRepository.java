package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.Event;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,String> {

    @Query("select distinct event from Event event left join fetch event.participants")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.participants where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") String id);

}
