package org.contacomigo.service.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.contacomigo.service.events.domain.DayOfWeek;

import org.contacomigo.service.events.repository.DayOfWeekRepository;
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
 * REST controller for managing DayOfWeek.
 */
@RestController
@RequestMapping("/api")
public class DayOfWeekResource {

    private final Logger log = LoggerFactory.getLogger(DayOfWeekResource.class);

    private static final String ENTITY_NAME = "dayOfWeek";
        
    private final DayOfWeekRepository dayOfWeekRepository;

    public DayOfWeekResource(DayOfWeekRepository dayOfWeekRepository) {
        this.dayOfWeekRepository = dayOfWeekRepository;
    }

    /**
     * POST  /day-of-weeks : Create a new dayOfWeek.
     *
     * @param dayOfWeek the dayOfWeek to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayOfWeek, or with status 400 (Bad Request) if the dayOfWeek has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/day-of-weeks")
    @Timed
    public ResponseEntity<DayOfWeek> createDayOfWeek(@Valid @RequestBody DayOfWeek dayOfWeek) throws URISyntaxException {
        log.debug("REST request to save DayOfWeek : {}", dayOfWeek);
        if (dayOfWeek.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dayOfWeek cannot already have an ID")).body(null);
        }
        DayOfWeek result = dayOfWeekRepository.save(dayOfWeek);
        return ResponseEntity.created(new URI("/api/day-of-weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /day-of-weeks : Updates an existing dayOfWeek.
     *
     * @param dayOfWeek the dayOfWeek to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayOfWeek,
     * or with status 400 (Bad Request) if the dayOfWeek is not valid,
     * or with status 500 (Internal Server Error) if the dayOfWeek couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/day-of-weeks")
    @Timed
    public ResponseEntity<DayOfWeek> updateDayOfWeek(@Valid @RequestBody DayOfWeek dayOfWeek) throws URISyntaxException {
        log.debug("REST request to update DayOfWeek : {}", dayOfWeek);
        if (dayOfWeek.getId() == null) {
            return createDayOfWeek(dayOfWeek);
        }
        DayOfWeek result = dayOfWeekRepository.save(dayOfWeek);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dayOfWeek.getId().toString()))
            .body(result);
    }

    /**
     * GET  /day-of-weeks : get all the dayOfWeeks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dayOfWeeks in body
     */
    @GetMapping("/day-of-weeks")
    @Timed
    public List<DayOfWeek> getAllDayOfWeeks() {
        log.debug("REST request to get all DayOfWeeks");
        List<DayOfWeek> dayOfWeeks = dayOfWeekRepository.findAll();
        return dayOfWeeks;
    }

    /**
     * GET  /day-of-weeks/:id : get the "id" dayOfWeek.
     *
     * @param id the id of the dayOfWeek to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dayOfWeek, or with status 404 (Not Found)
     */
    @GetMapping("/day-of-weeks/{id}")
    @Timed
    public ResponseEntity<DayOfWeek> getDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to get DayOfWeek : {}", id);
        DayOfWeek dayOfWeek = dayOfWeekRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dayOfWeek));
    }

    /**
     * DELETE  /day-of-weeks/:id : delete the "id" dayOfWeek.
     *
     * @param id the id of the dayOfWeek to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/day-of-weeks/{id}")
    @Timed
    public ResponseEntity<Void> deleteDayOfWeek(@PathVariable Long id) {
        log.debug("REST request to delete DayOfWeek : {}", id);
        dayOfWeekRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
