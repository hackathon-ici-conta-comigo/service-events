package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.DayOfWeek;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DayOfWeek entity.
 */
@SuppressWarnings("unused")
public interface DayOfWeekRepository extends JpaRepository<DayOfWeek,Long> {

}
