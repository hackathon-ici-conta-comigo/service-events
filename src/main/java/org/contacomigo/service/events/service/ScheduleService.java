package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.Schedule;
import org.contacomigo.service.events.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Schedule.
 */
@Service
@Transactional
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Save a schedule.
     *
     * @param schedule the entity to save
     * @return the persisted entity
     */
    public Schedule save(Schedule schedule) {
        log.debug("Request to save Schedule : {}", schedule);
        Schedule result = scheduleRepository.save(schedule);
        return result;
    }

    /**
     *  Get all the schedules.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Schedule> findAll() {
        log.debug("Request to get all Schedules");
        List<Schedule> result = scheduleRepository.findAll();

        return result;
    }

    /**
     *  Get one schedule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Schedule findOne(String id) {
        log.debug("Request to get Schedule : {}", id);
        Schedule schedule = scheduleRepository.findOne(id);
        return schedule;
    }

    /**
     *  Delete the  schedule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.delete(id);
    }
}
