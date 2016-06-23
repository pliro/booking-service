package io.aurora.ams.web.rest;

import io.aurora.ams.PliroApp;
import io.aurora.ams.domain.Booking;
import io.aurora.ams.repository.BookingRepository;
import io.aurora.ams.service.BookingService;
import io.aurora.ams.web.rest.dto.BookingDTO;
import io.aurora.ams.web.rest.mapper.BookingMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurora.ams.domain.enumeration.BookingState;

/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PliroApp.class)
@WebAppConfiguration
@IntegrationTest
public class BookingResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_BOOKING_ID = "AAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRES_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EXPIRES_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EXPIRES_AT_STR = dateTimeFormatter.format(DEFAULT_EXPIRES_AT);

    private static final BookingState DEFAULT_STATE = BookingState.AWAITING_VERIFICATION;
    private static final BookingState UPDATED_STATE = BookingState.VERIFICATION_EXPIRED;
    private static final String DEFAULT_SLOT_ID = "AAAAA";
    private static final String UPDATED_SLOT_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_BOOKED_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BOOKED_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BOOKED_FROM_STR = dateTimeFormatter.format(DEFAULT_BOOKED_FROM);

    private static final ZonedDateTime DEFAULT_BOOKED_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BOOKED_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BOOKED_TO_STR = dateTimeFormatter.format(DEFAULT_BOOKED_TO);
    private static final String DEFAULT_RESOURCE_ID = "AAAAA";
    private static final String UPDATED_RESOURCE_ID = "BBBBB";
    private static final String DEFAULT_APPOINTMENT_ID = "AAAAA";
    private static final String UPDATED_APPOINTMENT_ID = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_PERSON_ID = "AAAAA";
    private static final String UPDATED_PERSON_ID = "BBBBB";
    private static final String DEFAULT_VERIFICATION_CODE_ID = "AAAAA";
    private static final String UPDATED_VERIFICATION_CODE_ID = "BBBBB";
    private static final String DEFAULT_PAYMENT_ID = "AAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBB";

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private BookingMapper bookingMapper;

    @Inject
    private BookingService bookingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingResource bookingResource = new BookingResource();
        ReflectionTestUtils.setField(bookingResource, "bookingService", bookingService);
        ReflectionTestUtils.setField(bookingResource, "bookingMapper", bookingMapper);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        booking = new Booking();
        booking.setBookingId(DEFAULT_BOOKING_ID);
        booking.setExpiresAt(DEFAULT_EXPIRES_AT);
        booking.setState(DEFAULT_STATE);
        booking.setSlotId(DEFAULT_SLOT_ID);
        booking.setBookedFrom(DEFAULT_BOOKED_FROM);
        booking.setBookedTo(DEFAULT_BOOKED_TO);
        booking.setResourceId(DEFAULT_RESOURCE_ID);
        booking.setAppointmentId(DEFAULT_APPOINTMENT_ID);
        booking.setActive(DEFAULT_ACTIVE);
        booking.setNotes(DEFAULT_NOTES);
        booking.setPersonId(DEFAULT_PERSON_ID);
        booking.setVerificationCodeId(DEFAULT_VERIFICATION_CODE_ID);
        booking.setPaymentId(DEFAULT_PAYMENT_ID);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testBooking.getExpiresAt()).isEqualTo(DEFAULT_EXPIRES_AT);
        assertThat(testBooking.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBooking.getSlotId()).isEqualTo(DEFAULT_SLOT_ID);
        assertThat(testBooking.getBookedFrom()).isEqualTo(DEFAULT_BOOKED_FROM);
        assertThat(testBooking.getBookedTo()).isEqualTo(DEFAULT_BOOKED_TO);
        assertThat(testBooking.getResourceId()).isEqualTo(DEFAULT_RESOURCE_ID);
        assertThat(testBooking.getAppointmentId()).isEqualTo(DEFAULT_APPOINTMENT_ID);
        assertThat(testBooking.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testBooking.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBooking.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testBooking.getVerificationCodeId()).isEqualTo(DEFAULT_VERIFICATION_CODE_ID);
        assertThat(testBooking.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
    }

    @Test
    @Transactional
    public void checkBookingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookingId(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiresAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setExpiresAt(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setState(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSlotIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setSlotId(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookedFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookedFrom(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookedToIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookedTo(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResourceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setResourceId(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(booking);

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookings
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
                .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID.toString())))
                .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT_STR)))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].slotId").value(hasItem(DEFAULT_SLOT_ID.toString())))
                .andExpect(jsonPath("$.[*].bookedFrom").value(hasItem(DEFAULT_BOOKED_FROM_STR)))
                .andExpect(jsonPath("$.[*].bookedTo").value(hasItem(DEFAULT_BOOKED_TO_STR)))
                .andExpect(jsonPath("$.[*].resourceId").value(hasItem(DEFAULT_RESOURCE_ID.toString())))
                .andExpect(jsonPath("$.[*].appointmentId").value(hasItem(DEFAULT_APPOINTMENT_ID.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.toString())))
                .andExpect(jsonPath("$.[*].verificationCodeId").value(hasItem(DEFAULT_VERIFICATION_CODE_ID.toString())))
                .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID.toString())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT_STR))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.slotId").value(DEFAULT_SLOT_ID.toString()))
            .andExpect(jsonPath("$.bookedFrom").value(DEFAULT_BOOKED_FROM_STR))
            .andExpect(jsonPath("$.bookedTo").value(DEFAULT_BOOKED_TO_STR))
            .andExpect(jsonPath("$.resourceId").value(DEFAULT_RESOURCE_ID.toString()))
            .andExpect(jsonPath("$.appointmentId").value(DEFAULT_APPOINTMENT_ID.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.toString()))
            .andExpect(jsonPath("$.verificationCodeId").value(DEFAULT_VERIFICATION_CODE_ID.toString()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = new Booking();
        updatedBooking.setId(booking.getId());
        updatedBooking.setBookingId(UPDATED_BOOKING_ID);
        updatedBooking.setExpiresAt(UPDATED_EXPIRES_AT);
        updatedBooking.setState(UPDATED_STATE);
        updatedBooking.setSlotId(UPDATED_SLOT_ID);
        updatedBooking.setBookedFrom(UPDATED_BOOKED_FROM);
        updatedBooking.setBookedTo(UPDATED_BOOKED_TO);
        updatedBooking.setResourceId(UPDATED_RESOURCE_ID);
        updatedBooking.setAppointmentId(UPDATED_APPOINTMENT_ID);
        updatedBooking.setActive(UPDATED_ACTIVE);
        updatedBooking.setNotes(UPDATED_NOTES);
        updatedBooking.setPersonId(UPDATED_PERSON_ID);
        updatedBooking.setVerificationCodeId(UPDATED_VERIFICATION_CODE_ID);
        updatedBooking.setPaymentId(UPDATED_PAYMENT_ID);
        BookingDTO bookingDTO = bookingMapper.bookingToBookingDTO(updatedBooking);

        restBookingMockMvc.perform(put("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
                .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testBooking.getExpiresAt()).isEqualTo(UPDATED_EXPIRES_AT);
        assertThat(testBooking.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBooking.getSlotId()).isEqualTo(UPDATED_SLOT_ID);
        assertThat(testBooking.getBookedFrom()).isEqualTo(UPDATED_BOOKED_FROM);
        assertThat(testBooking.getBookedTo()).isEqualTo(UPDATED_BOOKED_TO);
        assertThat(testBooking.getResourceId()).isEqualTo(UPDATED_RESOURCE_ID);
        assertThat(testBooking.getAppointmentId()).isEqualTo(UPDATED_APPOINTMENT_ID);
        assertThat(testBooking.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testBooking.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBooking.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testBooking.getVerificationCodeId()).isEqualTo(UPDATED_VERIFICATION_CODE_ID);
        assertThat(testBooking.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
