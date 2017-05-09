package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.Period;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Period entity.
 */
@SuppressWarnings("unused")
public interface PeriodRepository extends JpaRepository<Period,Long> {

}
