package io.aurora.ams.service;

import io.aurora.ams.domain.Person;
import io.aurora.ams.web.rest.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Person.
 */
public interface PersonService {

    /**
     * Save a person.
     * 
     * @param personDTO the entity to save
     * @return the persisted entity
     */
    PersonDTO save(PersonDTO personDTO);

    /**
     *  Get all the people.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Person> findAll(Pageable pageable);

    /**
     *  Get the "id" person.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PersonDTO findOne(Long id);

    /**
     *  Delete the "id" person.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
