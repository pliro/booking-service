package io.aurora.ams.web.rest;

import io.aurora.ams.PliroApp;
import io.aurora.ams.domain.Payment;
import io.aurora.ams.repository.PaymentRepository;
import io.aurora.ams.service.PaymentService;
import io.aurora.ams.web.rest.dto.PaymentDTO;
import io.aurora.ams.web.rest.mapper.PaymentMapper;

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
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PliroApp.class)
@WebAppConfiguration
@IntegrationTest
public class PaymentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_PAYMENT_ID = "AAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_PAID_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PAID_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PAID_AT_STR = dateTimeFormatter.format(DEFAULT_PAID_AT);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private PaymentMapper paymentMapper;

    @Inject
    private PaymentService paymentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaymentResource paymentResource = new PaymentResource();
        ReflectionTestUtils.setField(paymentResource, "paymentService", paymentService);
        ReflectionTestUtils.setField(paymentResource, "paymentMapper", paymentMapper);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        payment = new Payment();
        payment.setPaymentId(DEFAULT_PAYMENT_ID);
        payment.setPaidAt(DEFAULT_PAID_AT);
        payment.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.paymentToPaymentDTO(payment);

        restPaymentMockMvc.perform(post("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
                .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = payments.get(payments.size() - 1);
        assertThat(testPayment.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testPayment.getPaidAt()).isEqualTo(DEFAULT_PAID_AT);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkPaymentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setPaymentId(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.paymentToPaymentDTO(payment);

        restPaymentMockMvc.perform(post("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
                .andExpect(status().isBadRequest());

        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the payments
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
                .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID.toString())))
                .andExpect(jsonPath("$.[*].paidAt").value(hasItem(DEFAULT_PAID_AT_STR)))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID.toString()))
            .andExpect(jsonPath("$.paidAt").value(DEFAULT_PAID_AT_STR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = new Payment();
        updatedPayment.setId(payment.getId());
        updatedPayment.setPaymentId(UPDATED_PAYMENT_ID);
        updatedPayment.setPaidAt(UPDATED_PAID_AT);
        updatedPayment.setAmount(UPDATED_AMOUNT);
        PaymentDTO paymentDTO = paymentMapper.paymentToPaymentDTO(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
                .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = payments.get(payments.size() - 1);
        assertThat(testPayment.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testPayment.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Get the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
