package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.Schedule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Schedule entity.
 */
@SuppressWarnings("unused")
public interface ScheduleRepository extends JpaRepository<Schedule,String> {

}
