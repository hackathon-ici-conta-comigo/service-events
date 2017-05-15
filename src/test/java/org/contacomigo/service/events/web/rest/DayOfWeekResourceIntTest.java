package org.contacomigo.service.events.web.rest;

import org.contacomigo.service.events.EventsApp;

import org.contacomigo.service.events.config.SecurityBeanOverrideConfiguration;

import org.contacomigo.service.events.domain.DayOfWeek;
import org.contacomigo.service.events.repository.DayOfWeekRepository;
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

import org.contacomigo.service.events.domain.enumeration.DaysOfWeek;
/**
 * Test class for the DayOfWeekResource REST controller.
 *
 * @see DayOfWeekResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApp.class, SecurityBeanOverrideConfiguration.class})
public class DayOfWeekResourceIntTest {

    private static final DaysOfWeek DEFAULT_DAY = DaysOfWeek.MONDAY;
    private static final DaysOfWeek UPDATED_DAY = DaysOfWeek.TUESDAY;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDayOfWeekMockMvc;

    private DayOfWeek dayOfWeek;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DayOfWeekResource dayOfWeekResource = new DayOfWeekResource(dayOfWeekRepository);
        this.restDayOfWeekMockMvc = MockMvcBuilders.standaloneSetup(dayOfWeekResource)
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
    public static DayOfWeek createEntity(EntityManager em) {
        DayOfWeek dayOfWeek = new DayOfWeek()
            .day(DEFAULT_DAY);
        return dayOfWeek;
    }

    @Before
    public void initTest() {
        dayOfWeek = createEntity(em);
    }

    @Test
    @Transactional
    public void createDayOfWeek() throws Exception {
        int databaseSizeBeforeCreate = dayOfWeekRepository.findAll().size();

        // Create the DayOfWeek
        restDayOfWeekMockMvc.perform(post("/api/day-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayOfWeek)))
            .andExpect(status().isCreated());

        // Validate the DayOfWeek in the database
        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeCreate + 1);
        DayOfWeek testDayOfWeek = dayOfWeekList.get(dayOfWeekList.size() - 1);
        assertThat(testDayOfWeek.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    public void createDayOfWeekWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dayOfWeekRepository.findAll().size();

        // Create the DayOfWeek with an existing ID
        dayOfWeek.setId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayOfWeekMockMvc.perform(post("/api/day-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayOfWeek)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayOfWeekRepository.findAll().size();
        // set the field null
        dayOfWeek.setDay(null);

        // Create the DayOfWeek, which fails.

        restDayOfWeekMockMvc.perform(post("/api/day-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayOfWeek)))
            .andExpect(status().isBadRequest());

        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDayOfWeeks() throws Exception {
        // Initialize the database
        dayOfWeekRepository.saveAndFlush(dayOfWeek);

        // Get all the dayOfWeekList
        restDayOfWeekMockMvc.perform(get("/api/day-of-weeks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayOfWeek.getId())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
    }

    @Test
    @Transactional
    public void getDayOfWeek() throws Exception {
        // Initialize the database
        dayOfWeekRepository.saveAndFlush(dayOfWeek);

        // Get the dayOfWeek
        restDayOfWeekMockMvc.perform(get("/api/day-of-weeks/{id}", dayOfWeek.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dayOfWeek.getId()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDayOfWeek() throws Exception {
        // Get the dayOfWeek
        restDayOfWeekMockMvc.perform(get("/api/day-of-weeks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDayOfWeek() throws Exception {
        // Initialize the database
        dayOfWeekRepository.saveAndFlush(dayOfWeek);
        int databaseSizeBeforeUpdate = dayOfWeekRepository.findAll().size();

        // Update the dayOfWeek
        DayOfWeek updatedDayOfWeek = dayOfWeekRepository.findOne(dayOfWeek.getId());
        updatedDayOfWeek
            .day(UPDATED_DAY);

        restDayOfWeekMockMvc.perform(put("/api/day-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDayOfWeek)))
            .andExpect(status().isOk());

        // Validate the DayOfWeek in the database
        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeUpdate);
        DayOfWeek testDayOfWeek = dayOfWeekList.get(dayOfWeekList.size() - 1);
        assertThat(testDayOfWeek.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingDayOfWeek() throws Exception {
        int databaseSizeBeforeUpdate = dayOfWeekRepository.findAll().size();

        // Create the DayOfWeek

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDayOfWeekMockMvc.perform(put("/api/day-of-weeks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayOfWeek)))
            .andExpect(status().isCreated());

        // Validate the DayOfWeek in the database
        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDayOfWeek() throws Exception {
        // Initialize the database
        dayOfWeekRepository.saveAndFlush(dayOfWeek);
        int databaseSizeBeforeDelete = dayOfWeekRepository.findAll().size();

        // Get the dayOfWeek
        restDayOfWeekMockMvc.perform(delete("/api/day-of-weeks/{id}", dayOfWeek.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        assertThat(dayOfWeekList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DayOfWeek.class);
    }
}
