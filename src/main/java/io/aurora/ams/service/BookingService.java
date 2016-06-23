package io.aurora.ams.service;

import io.aurora.ams.domain.Booking;
import io.aurora.ams.web.rest.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Booking.
 */
public interface BookingService {

    /**
     * Save a booking.
     * 
     * @param bookingDTO the entity to save
     * @return the persisted entity
     */
    BookingDTO save(BookingDTO bookingDTO);

    /**
     *  Get all the bookings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Booking> findAll(Pageable pageable);

    /**
     *  Get the "id" booking.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    BookingDTO findOne(Long id);

    /**
     *  Delete the "id" booking.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
