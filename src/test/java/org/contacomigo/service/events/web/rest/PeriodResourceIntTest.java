package org.contacomigo.service.events.web.rest;

import org.contacomigo.service.events.EventsApp;

import org.contacomigo.service.events.config.SecurityBeanOverrideConfiguration;

import org.contacomigo.service.events.domain.Period;
import org.contacomigo.service.events.repository.PeriodRepository;
import org.contacomigo.service.events.service.PeriodService;
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
 * Test class for the PeriodResource REST controller.
 *
 * @see PeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApp.class, SecurityBeanOverrideConfiguration.class})
public class PeriodResourceIntTest {

    private static final Integer DEFAULT_INTERVAL = 1;
    private static final Integer UPDATED_INTERVAL = 2;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeriodMockMvc;

    private Period period;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeriodResource periodResource = new PeriodResource(periodService);
        this.restPeriodMockMvc = MockMvcBuilders.standaloneSetup(periodResource)
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
    public static Period createEntity(EntityManager em) {
        Period period = new Period()
            .interval(DEFAULT_INTERVAL);
        return period;
    }

    @Before
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriod() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getInterval()).isEqualTo(DEFAULT_INTERVAL);
    }

    @Test
    @Transactional
    public void createPeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period with an existing ID
        period.setId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIntervalIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setInterval(null);

        // Create the Period, which fails.

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL)));
    }

    @Test
    @Transactional
    public void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL));
    }

    @Test
    @Transactional
    public void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriod() throws Exception {
        // Initialize the database
        periodService.save(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period
        Period updatedPeriod = periodRepository.findOne(period.getId());
        updatedPeriod
            .interval(UPDATED_INTERVAL);

        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriod)))
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getInterval()).isEqualTo(UPDATED_INTERVAL);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Create the Period

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeriod() throws Exception {
        // Initialize the database
        periodService.save(period);

        int databaseSizeBeforeDelete = periodRepository.findAll().size();

        // Get the period
        restPeriodMockMvc.perform(delete("/api/periods/{id}", period.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
    }
}
