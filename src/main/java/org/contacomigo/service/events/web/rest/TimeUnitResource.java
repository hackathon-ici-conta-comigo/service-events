package org.contacomigo.service.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.contacomigo.service.events.domain.TimeUnit;
import org.contacomigo.service.events.service.TimeUnitService;
import org.contacomigo.service.events.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TimeUnit.
 */
@RestController
@RequestMapping("/api")
public class TimeUnitResource {

    private final Logger log = LoggerFactory.getLogger(TimeUnitResource.class);

    private static final String ENTITY_NAME = "timeUnit";
        
    private final TimeUnitService timeUnitService;

    public TimeUnitResource(TimeUnitService timeUnitService) {
        this.timeUnitService = timeUnitService;
    }

    /**
     * POST  /time-units : Create a new timeUnit.
     *
     * @param timeUnit the timeUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeUnit, or with status 400 (Bad Request) if the timeUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-units")
    @Timed
    public ResponseEntity<TimeUnit> createTimeUnit(@Valid @RequestBody TimeUnit timeUnit) throws URISyntaxException {
        log.debug("REST request to save TimeUnit : {}", timeUnit);
        if (timeUnit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new timeUnit cannot already have an ID")).body(null);
        }
        TimeUnit result = timeUnitService.save(timeUnit);
        return ResponseEntity.created(new URI("/api/time-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-units : Updates an existing timeUnit.
     *
     * @param timeUnit the timeUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeUnit,
     * or with status 400 (Bad Request) if the timeUnit is not valid,
     * or with status 500 (Internal Server Error) if the timeUnit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-units")
    @Timed
    public ResponseEntity<TimeUnit> updateTimeUnit(@Valid @RequestBody TimeUnit timeUnit) throws URISyntaxException {
        log.debug("REST request to update TimeUnit : {}", timeUnit);
        if (timeUnit.getId() == null) {
            return createTimeUnit(timeUnit);
        }
        TimeUnit result = timeUnitService.save(timeUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-units : get all the timeUnits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timeUnits in body
     */
    @GetMapping("/time-units")
    @Timed
    public List<TimeUnit> getAllTimeUnits() {
        log.debug("REST request to get all TimeUnits");
        return timeUnitService.findAll();
    }

    /**
     * GET  /time-units/:id : get the "id" timeUnit.
     *
     * @param id the id of the timeUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeUnit, or with status 404 (Not Found)
     */
    @GetMapping("/time-units/{id}")
    @Timed
    public ResponseEntity<TimeUnit> getTimeUnit(@PathVariable String id) {
        log.debug("REST request to get TimeUnit : {}", id);
        TimeUnit timeUnit = timeUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timeUnit));
    }

    /**
     * DELETE  /time-units/:id : delete the "id" timeUnit.
     *
     * @param id the id of the timeUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-units/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeUnit(@PathVariable String id) {
        log.debug("REST request to delete TimeUnit : {}", id);
        timeUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
