package org.contacomigo.service.events.web.rest;

import org.contacomigo.service.events.EventsApp;

import org.contacomigo.service.events.config.SecurityBeanOverrideConfiguration;

import org.contacomigo.service.events.domain.TimeUnit;
import org.contacomigo.service.events.repository.TimeUnitRepository;
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

import org.contacomigo.service.events.domain.enumeration.TimeUnits;
/**
 * Test class for the TimeUnitResource REST controller.
 *
 * @see TimeUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApp.class, SecurityBeanOverrideConfiguration.class})
public class TimeUnitResourceIntTest {

    private static final TimeUnits DEFAULT_UNIT_TIME = TimeUnits.DAY;
    private static final TimeUnits UPDATED_UNIT_TIME = TimeUnits.WEEK;

    @Autowired
    private TimeUnitRepository timeUnitRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeUnitMockMvc;

    private TimeUnit timeUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimeUnitResource timeUnitResource = new TimeUnitResource(timeUnitRepository);
        this.restTimeUnitMockMvc = MockMvcBuilders.standaloneSetup(timeUnitResource)
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
    public static TimeUnit createEntity(EntityManager em) {
        TimeUnit timeUnit = new TimeUnit()
            .unitTime(DEFAULT_UNIT_TIME);
        return timeUnit;
    }

    @Before
    public void initTest() {
        timeUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeUnit() throws Exception {
        int databaseSizeBeforeCreate = timeUnitRepository.findAll().size();

        // Create the TimeUnit
        restTimeUnitMockMvc.perform(post("/api/time-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeUnit)))
            .andExpect(status().isCreated());

        // Validate the TimeUnit in the database
        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeCreate + 1);
        TimeUnit testTimeUnit = timeUnitList.get(timeUnitList.size() - 1);
        assertThat(testTimeUnit.getUnitTime()).isEqualTo(DEFAULT_UNIT_TIME);
    }

    @Test
    @Transactional
    public void createTimeUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeUnitRepository.findAll().size();

        // Create the TimeUnit with an existing ID
        timeUnit.setId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeUnitMockMvc.perform(post("/api/time-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeUnit)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUnitTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeUnitRepository.findAll().size();
        // set the field null
        timeUnit.setUnitTime(null);

        // Create the TimeUnit, which fails.

        restTimeUnitMockMvc.perform(post("/api/time-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeUnit)))
            .andExpect(status().isBadRequest());

        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimeUnits() throws Exception {
        // Initialize the database
        timeUnitRepository.saveAndFlush(timeUnit);

        // Get all the timeUnitList
        restTimeUnitMockMvc.perform(get("/api/time-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeUnit.getId())))
            .andExpect(jsonPath("$.[*].unitTime").value(hasItem(DEFAULT_UNIT_TIME.toString())));
    }

    @Test
    @Transactional
    public void getTimeUnit() throws Exception {
        // Initialize the database
        timeUnitRepository.saveAndFlush(timeUnit);

        // Get the timeUnit
        restTimeUnitMockMvc.perform(get("/api/time-units/{id}", timeUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeUnit.getId()))
            .andExpect(jsonPath("$.unitTime").value(DEFAULT_UNIT_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimeUnit() throws Exception {
        // Get the timeUnit
        restTimeUnitMockMvc.perform(get("/api/time-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeUnit() throws Exception {
        // Initialize the database
        timeUnitRepository.saveAndFlush(timeUnit);
        int databaseSizeBeforeUpdate = timeUnitRepository.findAll().size();

        // Update the timeUnit
        TimeUnit updatedTimeUnit = timeUnitRepository.findOne(timeUnit.getId());
        updatedTimeUnit
            .unitTime(UPDATED_UNIT_TIME);

        restTimeUnitMockMvc.perform(put("/api/time-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimeUnit)))
            .andExpect(status().isOk());

        // Validate the TimeUnit in the database
        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeUpdate);
        TimeUnit testTimeUnit = timeUnitList.get(timeUnitList.size() - 1);
        assertThat(testTimeUnit.getUnitTime()).isEqualTo(UPDATED_UNIT_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingTimeUnit() throws Exception {
        int databaseSizeBeforeUpdate = timeUnitRepository.findAll().size();

        // Create the TimeUnit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeUnitMockMvc.perform(put("/api/time-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeUnit)))
            .andExpect(status().isCreated());

        // Validate the TimeUnit in the database
        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimeUnit() throws Exception {
        // Initialize the database
        timeUnitRepository.saveAndFlush(timeUnit);
        int databaseSizeBeforeDelete = timeUnitRepository.findAll().size();

        // Get the timeUnit
        restTimeUnitMockMvc.perform(delete("/api/time-units/{id}", timeUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TimeUnit> timeUnitList = timeUnitRepository.findAll();
        assertThat(timeUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeUnit.class);
    }
}
