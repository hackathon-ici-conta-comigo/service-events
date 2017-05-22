package org.contacomigo.service.events.web.rest;

import org.contacomigo.service.events.EventsApp;

import org.contacomigo.service.events.config.SecurityBeanOverrideConfiguration;

import org.contacomigo.service.events.domain.ParticipantUserProfile;
import org.contacomigo.service.events.repository.ParticipantUserProfileRepository;
import org.contacomigo.service.events.service.ParticipantUserProfileService;
import org.contacomigo.service.events.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParticipantUserProfileResource REST controller.
 *
 * @see ParticipantUserProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApp.class, SecurityBeanOverrideConfiguration.class})
public class ParticipantUserProfileResourceIntTest {

    @Autowired
    private ParticipantUserProfileRepository participantUserProfileRepository;

    @Autowired
    private ParticipantUserProfileService participantUserProfileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParticipantUserProfileMockMvc;

    private ParticipantUserProfile participantUserProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipantUserProfileResource participantUserProfileResource = new ParticipantUserProfileResource(participantUserProfileService);
        this.restParticipantUserProfileMockMvc = MockMvcBuilders.standaloneSetup(participantUserProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipantUserProfile createEntity(EntityManager em) {
        ParticipantUserProfile participantUserProfile = new ParticipantUserProfile();
        return participantUserProfile;
    }

    @Before
    public void initTest() {
        participantUserProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipantUserProfile() throws Exception {
        int databaseSizeBeforeCreate = participantUserProfileRepository.findAll().size();

        // Create the ParticipantUserProfile
        restParticipantUserProfileMockMvc.perform(post("/api/participant-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantUserProfile)))
            .andExpect(status().isCreated());

        // Validate the ParticipantUserProfile in the database
        List<ParticipantUserProfile> participantUserProfileList = participantUserProfileRepository.findAll();
        assertThat(participantUserProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ParticipantUserProfile testParticipantUserProfile = participantUserProfileList.get(participantUserProfileList.size() - 1);
    }

    @Test
    @Transactional
    public void createParticipantUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantUserProfileRepository.findAll().size();

        // Create the ParticipantUserProfile with an existing ID
        participantUserProfile.setId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantUserProfileMockMvc.perform(post("/api/participant-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantUserProfile)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ParticipantUserProfile> participantUserProfileList = participantUserProfileRepository.findAll();
        assertThat(participantUserProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParticipantUserProfiles() throws Exception {
        // Initialize the database
        participantUserProfileRepository.saveAndFlush(participantUserProfile);

        // Get all the participantUserProfileList
        restParticipantUserProfileMockMvc.perform(get("/api/participant-user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participantUserProfile.getId())));
    }

    @Test
    @Transactional
    public void getParticipantUserProfile() throws Exception {
        // Initialize the database
        participantUserProfileRepository.saveAndFlush(participantUserProfile);

        // Get the participantUserProfile
        restParticipantUserProfileMockMvc.perform(get("/api/participant-user-profiles/{id}", participantUserProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participantUserProfile));
    }

    @Test
    @Transactional
    public void getNonExistingParticipantUserProfile() throws Exception {
        // Get the participantUserProfile
        restParticipantUserProfileMockMvc.perform(get("/api/participant-user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipantUserProfile() throws Exception {
        // Initialize the database
        participantUserProfileService.save(participantUserProfile);

        int databaseSizeBeforeUpdate = participantUserProfileRepository.findAll().size();

        // Update the participantUserProfile
        ParticipantUserProfile updatedParticipantUserProfile = participantUserProfileRepository.findOne(participantUserProfile.getId());

        restParticipantUserProfileMockMvc.perform(put("/api/participant-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipantUserProfile)))
            .andExpect(status().isOk());

        // Validate the ParticipantUserProfile in the database
        List<ParticipantUserProfile> participantUserProfileList = participantUserProfileRepository.findAll();
        assertThat(participantUserProfileList).hasSize(databaseSizeBeforeUpdate);
        ParticipantUserProfile testParticipantUserProfile = participantUserProfileList.get(participantUserProfileList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipantUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = participantUserProfileRepository.findAll().size();

        // Create the ParticipantUserProfile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParticipantUserProfileMockMvc.perform(put("/api/participant-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantUserProfile)))
            .andExpect(status().isCreated());

        // Validate the ParticipantUserProfile in the database
        List<ParticipantUserProfile> participantUserProfileList = participantUserProfileRepository.findAll();
        assertThat(participantUserProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParticipantUserProfile() throws Exception {
        // Initialize the database
        participantUserProfileService.save(participantUserProfile);

        int databaseSizeBeforeDelete = participantUserProfileRepository.findAll().size();

        // Get the participantUserProfile
        restParticipantUserProfileMockMvc.perform(delete("/api/participant-user-profiles/{id}", participantUserProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ParticipantUserProfile> participantUserProfileList = participantUserProfileRepository.findAll();
        assertThat(participantUserProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipantUserProfile.class);
    }
}
