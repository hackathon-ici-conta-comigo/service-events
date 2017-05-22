package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.ParticipantUserProfile;
import org.contacomigo.service.events.repository.ParticipantUserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ParticipantUserProfile.
 */
@Service
@Transactional
public class ParticipantUserProfileService {

    private final Logger log = LoggerFactory.getLogger(ParticipantUserProfileService.class);
    
    private final ParticipantUserProfileRepository participantUserProfileRepository;

    public ParticipantUserProfileService(ParticipantUserProfileRepository participantUserProfileRepository) {
        this.participantUserProfileRepository = participantUserProfileRepository;
    }

    /**
     * Save a participantUserProfile.
     *
     * @param participantUserProfile the entity to save
     * @return the persisted entity
     */
    public ParticipantUserProfile save(ParticipantUserProfile participantUserProfile) {
        log.debug("Request to save ParticipantUserProfile : {}", participantUserProfile);
        ParticipantUserProfile result = participantUserProfileRepository.save(participantUserProfile);
        return result;
    }

    /**
     *  Get all the participantUserProfiles.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ParticipantUserProfile> findAll() {
        log.debug("Request to get all ParticipantUserProfiles");
        List<ParticipantUserProfile> result = participantUserProfileRepository.findAll();

        return result;
    }

    /**
     *  Get one participantUserProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ParticipantUserProfile findOne(String id) {
        log.debug("Request to get ParticipantUserProfile : {}", id);
        ParticipantUserProfile participantUserProfile = participantUserProfileRepository.findOne(id);
        return participantUserProfile;
    }

    /**
     *  Delete the  participantUserProfile by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ParticipantUserProfile : {}", id);
        participantUserProfileRepository.delete(id);
    }
}
