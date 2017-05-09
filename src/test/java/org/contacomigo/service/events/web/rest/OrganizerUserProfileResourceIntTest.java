package org.contacomigo.service.events.web.rest;

import org.contacomigo.service.events.EventsApp;

import org.contacomigo.service.events.config.SecurityBeanOverrideConfiguration;

import org.contacomigo.service.events.domain.OrganizerUserProfile;
import org.contacomigo.service.events.repository.OrganizerUserProfileRepository;
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
 * Test class for the OrganizerUserProfileResource REST controller.
 *
 * @see OrganizerUserProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApp.class, SecurityBeanOverrideConfiguration.class})
public class OrganizerUserProfileResourceIntTest {

    @Autowired
    private OrganizerUserProfileRepository organizerUserProfileRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganizerUserProfileMockMvc;

    private OrganizerUserProfile organizerUserProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizerUserProfileResource organizerUserProfileResource = new OrganizerUserProfileResource(organizerUserProfileRepository);
        this.restOrganizerUserProfileMockMvc = MockMvcBuilders.standaloneSetup(organizerUserProfileResource)
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
    public static OrganizerUserProfile createEntity(EntityManager em) {
        OrganizerUserProfile organizerUserProfile = new OrganizerUserProfile();
        return organizerUserProfile;
    }

    @Before
    public void initTest() {
        organizerUserProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizerUserProfile() throws Exception {
        int databaseSizeBeforeCreate = organizerUserProfileRepository.findAll().size();

        // Create the OrganizerUserProfile
        restOrganizerUserProfileMockMvc.perform(post("/api/organizer-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizerUserProfile)))
            .andExpect(status().isCreated());

        // Validate the OrganizerUserProfile in the database
        List<OrganizerUserProfile> organizerUserProfileList = organizerUserProfileRepository.findAll();
        assertThat(organizerUserProfileList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizerUserProfile testOrganizerUserProfile = organizerUserProfileList.get(organizerUserProfileList.size() - 1);
    }

    @Test
    @Transactional
    public void createOrganizerUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizerUserProfileRepository.findAll().size();

        // Create the OrganizerUserProfile with an existing ID
        organizerUserProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizerUserProfileMockMvc.perform(post("/api/organizer-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizerUserProfile)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OrganizerUserProfile> organizerUserProfileList = organizerUserProfileRepository.findAll();
        assertThat(organizerUserProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrganizerUserProfiles() throws Exception {
        // Initialize the database
        organizerUserProfileRepository.saveAndFlush(organizerUserProfile);

        // Get all the organizerUserProfileList
        restOrganizerUserProfileMockMvc.perform(get("/api/organizer-user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizerUserProfile.getId().intValue())));
    }

    @Test
    @Transactional
    public void getOrganizerUserProfile() throws Exception {
        // Initialize the database
        organizerUserProfileRepository.saveAndFlush(organizerUserProfile);

        // Get the organizerUserProfile
        restOrganizerUserProfileMockMvc.perform(get("/api/organizer-user-profiles/{id}", organizerUserProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizerUserProfile.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizerUserProfile() throws Exception {
        // Get the organizerUserProfile
        restOrganizerUserProfileMockMvc.perform(get("/api/organizer-user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizerUserProfile() throws Exception {
        // Initialize the database
        organizerUserProfileRepository.saveAndFlush(organizerUserProfile);
        int databaseSizeBeforeUpdate = organizerUserProfileRepository.findAll().size();

        // Update the organizerUserProfile
        OrganizerUserProfile updatedOrganizerUserProfile = organizerUserProfileRepository.findOne(organizerUserProfile.getId());

        restOrganizerUserProfileMockMvc.perform(put("/api/organizer-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganizerUserProfile)))
            .andExpect(status().isOk());

        // Validate the OrganizerUserProfile in the database
        List<OrganizerUserProfile> organizerUserProfileList = organizerUserProfileRepository.findAll();
        assertThat(organizerUserProfileList).hasSize(databaseSizeBeforeUpdate);
        OrganizerUserProfile testOrganizerUserProfile = organizerUserProfileList.get(organizerUserProfileList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizerUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = organizerUserProfileRepository.findAll().size();

        // Create the OrganizerUserProfile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrganizerUserProfileMockMvc.perform(put("/api/organizer-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizerUserProfile)))
            .andExpect(status().isCreated());

        // Validate the OrganizerUserProfile in the database
        List<OrganizerUserProfile> organizerUserProfileList = organizerUserProfileRepository.findAll();
        assertThat(organizerUserProfileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrganizerUserProfile() throws Exception {
        // Initialize the database
        organizerUserProfileRepository.saveAndFlush(organizerUserProfile);
        int databaseSizeBeforeDelete = organizerUserProfileRepository.findAll().size();

        // Get the organizerUserProfile
        restOrganizerUserProfileMockMvc.perform(delete("/api/organizer-user-profiles/{id}", organizerUserProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrganizerUserProfile> organizerUserProfileList = organizerUserProfileRepository.findAll();
        assertThat(organizerUserProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizerUserProfile.class);
    }
}
