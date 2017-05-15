package org.contacomigo.service.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.contacomigo.service.events.domain.ParticipantUserProfile;

import org.contacomigo.service.events.repository.ParticipantUserProfileRepository;
import org.contacomigo.service.events.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ParticipantUserProfile.
 */
@RestController
@RequestMapping("/api")
public class ParticipantUserProfileResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantUserProfileResource.class);

    private static final String ENTITY_NAME = "participantUserProfile";
        
    private final ParticipantUserProfileRepository participantUserProfileRepository;

    public ParticipantUserProfileResource(ParticipantUserProfileRepository participantUserProfileRepository) {
        this.participantUserProfileRepository = participantUserProfileRepository;
    }

    /**
     * POST  /participant-user-profiles : Create a new participantUserProfile.
     *
     * @param participantUserProfile the participantUserProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participantUserProfile, or with status 400 (Bad Request) if the participantUserProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/participant-user-profiles")
    @Timed
    public ResponseEntity<ParticipantUserProfile> createParticipantUserProfile(@RequestBody ParticipantUserProfile participantUserProfile) throws URISyntaxException {
        log.debug("REST request to save ParticipantUserProfile : {}", participantUserProfile);
        if (participantUserProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new participantUserProfile cannot already have an ID")).body(null);
        }
        ParticipantUserProfile result = participantUserProfileRepository.save(participantUserProfile);
        return ResponseEntity.created(new URI("/api/participant-user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participant-user-profiles : Updates an existing participantUserProfile.
     *
     * @param participantUserProfile the participantUserProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participantUserProfile,
     * or with status 400 (Bad Request) if the participantUserProfile is not valid,
     * or with status 500 (Internal Server Error) if the participantUserProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/participant-user-profiles")
    @Timed
    public ResponseEntity<ParticipantUserProfile> updateParticipantUserProfile(@RequestBody ParticipantUserProfile participantUserProfile) throws URISyntaxException {
        log.debug("REST request to update ParticipantUserProfile : {}", participantUserProfile);
        if (participantUserProfile.getId() == null) {
            return createParticipantUserProfile(participantUserProfile);
        }
        ParticipantUserProfile result = participantUserProfileRepository.save(participantUserProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, participantUserProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participant-user-profiles : get all the participantUserProfiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of participantUserProfiles in body
     */
    @GetMapping("/participant-user-profiles")
    @Timed
    public List<ParticipantUserProfile> getAllParticipantUserProfiles() {
        log.debug("REST request to get all ParticipantUserProfiles");
        List<ParticipantUserProfile> participantUserProfiles = participantUserProfileRepository.findAll();
        return participantUserProfiles;
    }

    /**
     * GET  /participant-user-profiles/:id : get the "id" participantUserProfile.
     *
     * @param id the id of the participantUserProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participantUserProfile, or with status 404 (Not Found)
     */
    @GetMapping("/participant-user-profiles/{id}")
    @Timed
    public ResponseEntity<ParticipantUserProfile> getParticipantUserProfile(@PathVariable String id) {
        log.debug("REST request to get ParticipantUserProfile : {}", id);
        ParticipantUserProfile participantUserProfile = participantUserProfileRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(participantUserProfile));
    }

    /**
     * DELETE  /participant-user-profiles/:id : delete the "id" participantUserProfile.
     *
     * @param id the id of the participantUserProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/participant-user-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteParticipantUserProfile(@PathVariable String id) {
        log.debug("REST request to delete ParticipantUserProfile : {}", id);
        participantUserProfileRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
