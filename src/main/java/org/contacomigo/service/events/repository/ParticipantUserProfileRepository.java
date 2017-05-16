package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.ParticipantUserProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParticipantUserProfile entity.
 */
@SuppressWarnings("unused")
public interface ParticipantUserProfileRepository extends JpaRepository<ParticipantUserProfile,Long> {

}
