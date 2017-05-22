package org.contacomigo.service.events.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.contacomigo.service.events.domain.OrganizerUserProfile;
import org.contacomigo.service.events.service.OrganizerUserProfileService;
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
 * REST controller for managing OrganizerUserProfile.
 */
@RestController
@RequestMapping("/api")
public class OrganizerUserProfileResource {

    private final Logger log = LoggerFactory.getLogger(OrganizerUserProfileResource.class);

    private static final String ENTITY_NAME = "organizerUserProfile";
        
    private final OrganizerUserProfileService organizerUserProfileService;

    public OrganizerUserProfileResource(OrganizerUserProfileService organizerUserProfileService) {
        this.organizerUserProfileService = organizerUserProfileService;
    }

    /**
     * POST  /organizer-user-profiles : Create a new organizerUserProfile.
     *
     * @param organizerUserProfile the organizerUserProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizerUserProfile, or with status 400 (Bad Request) if the organizerUserProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organizer-user-profiles")
    @Timed
    public ResponseEntity<OrganizerUserProfile> createOrganizerUserProfile(@RequestBody OrganizerUserProfile organizerUserProfile) throws URISyntaxException {
        log.debug("REST request to save OrganizerUserProfile : {}", organizerUserProfile);
        if (organizerUserProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new organizerUserProfile cannot already have an ID")).body(null);
        }
        OrganizerUserProfile result = organizerUserProfileService.save(organizerUserProfile);
        return ResponseEntity.created(new URI("/api/organizer-user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organizer-user-profiles : Updates an existing organizerUserProfile.
     *
     * @param organizerUserProfile the organizerUserProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizerUserProfile,
     * or with status 400 (Bad Request) if the organizerUserProfile is not valid,
     * or with status 500 (Internal Server Error) if the organizerUserProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organizer-user-profiles")
    @Timed
    public ResponseEntity<OrganizerUserProfile> updateOrganizerUserProfile(@RequestBody OrganizerUserProfile organizerUserProfile) throws URISyntaxException {
        log.debug("REST request to update OrganizerUserProfile : {}", organizerUserProfile);
        if (organizerUserProfile.getId() == null) {
            return createOrganizerUserProfile(organizerUserProfile);
        }
        OrganizerUserProfile result = organizerUserProfileService.save(organizerUserProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizerUserProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organizer-user-profiles : get all the organizerUserProfiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizerUserProfiles in body
     */
    @GetMapping("/organizer-user-profiles")
    @Timed
    public List<OrganizerUserProfile> getAllOrganizerUserProfiles() {
        log.debug("REST request to get all OrganizerUserProfiles");
        return organizerUserProfileService.findAll();
    }

    /**
     * GET  /organizer-user-profiles/:id : get the "id" organizerUserProfile.
     *
     * @param id the id of the organizerUserProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizerUserProfile, or with status 404 (Not Found)
     */
    @GetMapping("/organizer-user-profiles/{id}")
    @Timed
    public ResponseEntity<OrganizerUserProfile> getOrganizerUserProfile(@PathVariable String id) {
        log.debug("REST request to get OrganizerUserProfile : {}", id);
        OrganizerUserProfile organizerUserProfile = organizerUserProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(organizerUserProfile));
    }

    /**
     * DELETE  /organizer-user-profiles/:id : delete the "id" organizerUserProfile.
     *
     * @param id the id of the organizerUserProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organizer-user-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganizerUserProfile(@PathVariable String id) {
        log.debug("REST request to delete OrganizerUserProfile : {}", id);
        organizerUserProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
