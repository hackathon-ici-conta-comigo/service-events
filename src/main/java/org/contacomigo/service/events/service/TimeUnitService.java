package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.TimeUnit;
import org.contacomigo.service.events.repository.TimeUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing TimeUnit.
 */
@Service
@Transactional
public class TimeUnitService {

    private final Logger log = LoggerFactory.getLogger(TimeUnitService.class);
    
    private final TimeUnitRepository timeUnitRepository;

    public TimeUnitService(TimeUnitRepository timeUnitRepository) {
        this.timeUnitRepository = timeUnitRepository;
    }

    /**
     * Save a timeUnit.
     *
     * @param timeUnit the entity to save
     * @return the persisted entity
     */
    public TimeUnit save(TimeUnit timeUnit) {
        log.debug("Request to save TimeUnit : {}", timeUnit);
        TimeUnit result = timeUnitRepository.save(timeUnit);
        return result;
    }

    /**
     *  Get all the timeUnits.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TimeUnit> findAll() {
        log.debug("Request to get all TimeUnits");
        List<TimeUnit> result = timeUnitRepository.findAll();

        return result;
    }

    /**
     *  Get one timeUnit by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TimeUnit findOne(String id) {
        log.debug("Request to get TimeUnit : {}", id);
        TimeUnit timeUnit = timeUnitRepository.findOne(id);
        return timeUnit;
    }

    /**
     *  Delete the  timeUnit by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete TimeUnit : {}", id);
        timeUnitRepository.delete(id);
    }
}
