package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.Period;
import org.contacomigo.service.events.repository.PeriodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Period.
 */
@Service
@Transactional
public class PeriodService {

    private final Logger log = LoggerFactory.getLogger(PeriodService.class);
    
    private final PeriodRepository periodRepository;

    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    /**
     * Save a period.
     *
     * @param period the entity to save
     * @return the persisted entity
     */
    public Period save(Period period) {
        log.debug("Request to save Period : {}", period);
        Period result = periodRepository.save(period);
        return result;
    }

    /**
     *  Get all the periods.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Period> findAll() {
        log.debug("Request to get all Periods");
        List<Period> result = periodRepository.findAll();

        return result;
    }

    /**
     *  Get one period by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Period findOne(String id) {
        log.debug("Request to get Period : {}", id);
        Period period = periodRepository.findOne(id);
        return period;
    }

    /**
     *  Delete the  period by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Period : {}", id);
        periodRepository.delete(id);
    }
}
