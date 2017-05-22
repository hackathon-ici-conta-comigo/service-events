package org.contacomigo.service.events.repository;

import org.contacomigo.service.events.domain.OrganizerUserProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrganizerUserProfile entity.
 */
@SuppressWarnings("unused")
public interface OrganizerUserProfileRepository extends JpaRepository<OrganizerUserProfile,String> {

}
