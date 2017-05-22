package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.OrganizerUserProfile;
import org.contacomigo.service.events.repository.OrganizerUserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing OrganizerUserProfile.
 */
@Service
@Transactional
public class OrganizerUserProfileService {

    private final Logger log = LoggerFactory.getLogger(OrganizerUserProfileService.class);
    
    private final OrganizerUserProfileRepository organizerUserProfileRepository;

    public OrganizerUserProfileService(OrganizerUserProfileRepository organizerUserProfileRepository) {
        this.organizerUserProfileRepository = organizerUserProfileRepository;
    }

    /**
     * Save a organizerUserProfile.
     *
     * @param organizerUserProfile the entity to save
     * @return the persisted entity
     */
    public OrganizerUserProfile save(OrganizerUserProfile organizerUserProfile) {
        log.debug("Request to save OrganizerUserProfile : {}", organizerUserProfile);
        OrganizerUserProfile result = organizerUserProfileRepository.save(organizerUserProfile);
        return result;
    }

    /**
     *  Get all the organizerUserProfiles.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrganizerUserProfile> findAll() {
        log.debug("Request to get all OrganizerUserProfiles");
        List<OrganizerUserProfile> result = organizerUserProfileRepository.findAll();

        return result;
    }

    /**
     *  Get one organizerUserProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OrganizerUserProfile findOne(String id) {
        log.debug("Request to get OrganizerUserProfile : {}", id);
        OrganizerUserProfile organizerUserProfile = organizerUserProfileRepository.findOne(id);
        return organizerUserProfile;
    }

    /**
     *  Delete the  organizerUserProfile by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete OrganizerUserProfile : {}", id);
        organizerUserProfileRepository.delete(id);
    }
}
