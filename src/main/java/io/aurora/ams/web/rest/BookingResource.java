package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.Booking;
import io.aurora.ams.service.BookingService;
import io.aurora.ams.web.rest.util.HeaderUtil;
import io.aurora.ams.web.rest.util.PaginationUtil;
import io.aurora.ams.web.rest.dto.BookingDTO;
import io.aurora.ams.web.rest.mapper.BookingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);
        
    @Inject
    private BookingService bookingService;
    
    @Inject
    private BookingMapper bookingMapper;
    
    /**
     * POST  /bookings : Create a new booking.
     *
     * @param bookingDTO the bookingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingDTO, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", bookingDTO);
        if (bookingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("booking", "idexists", "A new booking cannot already have an ID")).body(null);
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("booking", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bookings : Updates an existing booking.
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingDTO> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            return createBooking(bookingDTO);
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("booking", bookingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookings : get all the bookings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BookingDTO>> getAllBookings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bookings");
        Page<Booking> page = bookingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bookings");
        return new ResponseEntity<>(bookingMapper.bookingsToBookingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /bookings/:id : get the "id" booking.
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bookings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        BookingDTO bookingDTO = bookingService.findOne(id);
        return Optional.ofNullable(bookingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookings/:id : delete the "id" booking.
     *
     * @param id the id of the bookingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bookings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("booking", id.toString())).build();
    }

}
