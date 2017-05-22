package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.DayOfWeek;
import org.contacomigo.service.events.repository.DayOfWeekRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing DayOfWeek.
 */
@Service
@Transactional
public class DayOfWeekService {

    private final Logger log = LoggerFactory.getLogger(DayOfWeekService.class);
    
    private final DayOfWeekRepository dayOfWeekRepository;

    public DayOfWeekService(DayOfWeekRepository dayOfWeekRepository) {
        this.dayOfWeekRepository = dayOfWeekRepository;
    }

    /**
     * Save a dayOfWeek.
     *
     * @param dayOfWeek the entity to save
     * @return the persisted entity
     */
    public DayOfWeek save(DayOfWeek dayOfWeek) {
        log.debug("Request to save DayOfWeek : {}", dayOfWeek);
        DayOfWeek result = dayOfWeekRepository.save(dayOfWeek);
        return result;
    }

    /**
     *  Get all the dayOfWeeks.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DayOfWeek> findAll() {
        log.debug("Request to get all DayOfWeeks");
        List<DayOfWeek> result = dayOfWeekRepository.findAll();

        return result;
    }

    /**
     *  Get one dayOfWeek by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DayOfWeek findOne(String id) {
        log.debug("Request to get DayOfWeek : {}", id);
        DayOfWeek dayOfWeek = dayOfWeekRepository.findOne(id);
        return dayOfWeek;
    }

    /**
     *  Delete the  dayOfWeek by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete DayOfWeek : {}", id);
        dayOfWeekRepository.delete(id);
    }
}
