package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.TimeUnit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TimeUnit entity.
 */
@SuppressWarnings("unused")
public interface TimeUnitRepository extends JpaRepository<TimeUnit,String> {

}
