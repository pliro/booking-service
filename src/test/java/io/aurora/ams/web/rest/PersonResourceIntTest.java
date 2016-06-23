package io.aurora.ams.web.rest;

import io.aurora.ams.PliroApp;
import io.aurora.ams.domain.Person;
import io.aurora.ams.repository.PersonRepository;
import io.aurora.ams.service.PersonService;
import io.aurora.ams.web.rest.dto.PersonDTO;
import io.aurora.ams.web.rest.mapper.PersonMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PliroApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonResourceIntTest {

    private static final String DEFAULT_PERSON_ID = "AAAAA";
    private static final String UPDATED_PERSON_ID = "BBBBB";
    private static final String DEFAULT_FULL_NAME = "AAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonMapper personMapper;

    @Inject
    private PersonService personService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonMockMvc;

    private Person person;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonResource personResource = new PersonResource();
        ReflectionTestUtils.setField(personResource, "personService", personService);
        ReflectionTestUtils.setField(personResource, "personMapper", personMapper);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        person = new Person();
        person.setPersonId(DEFAULT_PERSON_ID);
        person.setFullName(DEFAULT_FULL_NAME);
        person.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        person.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.personToPersonDTO(person);

        restPersonMockMvc.perform(post("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personDTO)))
                .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = people.get(people.size() - 1);
        assertThat(testPerson.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testPerson.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPerson.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkPersonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setPersonId(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.personToPersonDTO(person);

        restPersonMockMvc.perform(post("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personDTO)))
                .andExpect(status().isBadRequest());

        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the people
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
                .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.toString())))
                .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = new Person();
        updatedPerson.setId(person.getId());
        updatedPerson.setPersonId(UPDATED_PERSON_ID);
        updatedPerson.setFullName(UPDATED_FULL_NAME);
        updatedPerson.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedPerson.setEmail(UPDATED_EMAIL);
        PersonDTO personDTO = personMapper.personToPersonDTO(updatedPerson);

        restPersonMockMvc.perform(put("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personDTO)))
                .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = people.get(people.size() - 1);
        assertThat(testPerson.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testPerson.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPerson.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Get the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeDelete - 1);
    }
}
