package io.aurora.ams.web.rest;

import io.aurora.ams.PliroApp;
import io.aurora.ams.domain.VerificationCode;
import io.aurora.ams.repository.VerificationCodeRepository;
import io.aurora.ams.service.VerificationCodeService;
import io.aurora.ams.web.rest.dto.VerificationCodeDTO;
import io.aurora.ams.web.rest.mapper.VerificationCodeMapper;

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

import io.aurora.ams.domain.enumeration.ContactPointSystem;

/**
 * Test class for the VerificationCodeResource REST controller.
 *
 * @see VerificationCodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PliroApp.class)
@WebAppConfiguration
@IntegrationTest
public class VerificationCodeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_VERIFICATION_CODE_ID = "AAAAA";
    private static final String UPDATED_VERIFICATION_CODE_ID = "BBBBB";

    private static final ContactPointSystem DEFAULT_SYSTEM = ContactPointSystem.PHONE;
    private static final ContactPointSystem UPDATED_SYSTEM = ContactPointSystem.FAX;
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_VERIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_VERIFIED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_VERIFIED_AT_STR = dateTimeFormatter.format(DEFAULT_VERIFIED_AT);

    @Inject
    private VerificationCodeRepository verificationCodeRepository;

    @Inject
    private VerificationCodeMapper verificationCodeMapper;

    @Inject
    private VerificationCodeService verificationCodeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVerificationCodeMockMvc;

    private VerificationCode verificationCode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VerificationCodeResource verificationCodeResource = new VerificationCodeResource();
        ReflectionTestUtils.setField(verificationCodeResource, "verificationCodeService", verificationCodeService);
        ReflectionTestUtils.setField(verificationCodeResource, "verificationCodeMapper", verificationCodeMapper);
        this.restVerificationCodeMockMvc = MockMvcBuilders.standaloneSetup(verificationCodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        verificationCode = new VerificationCode();
        verificationCode.setVerificationCodeId(DEFAULT_VERIFICATION_CODE_ID);
        verificationCode.setSystem(DEFAULT_SYSTEM);
        verificationCode.setValue(DEFAULT_VALUE);
        verificationCode.setCode(DEFAULT_CODE);
        verificationCode.setVerifiedAt(DEFAULT_VERIFIED_AT);
    }

    @Test
    @Transactional
    public void createVerificationCode() throws Exception {
        int databaseSizeBeforeCreate = verificationCodeRepository.findAll().size();

        // Create the VerificationCode
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);

        restVerificationCodeMockMvc.perform(post("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isCreated());

        // Validate the VerificationCode in the database
        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeCreate + 1);
        VerificationCode testVerificationCode = verificationCodes.get(verificationCodes.size() - 1);
        assertThat(testVerificationCode.getVerificationCodeId()).isEqualTo(DEFAULT_VERIFICATION_CODE_ID);
        assertThat(testVerificationCode.getSystem()).isEqualTo(DEFAULT_SYSTEM);
        assertThat(testVerificationCode.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testVerificationCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVerificationCode.getVerifiedAt()).isEqualTo(DEFAULT_VERIFIED_AT);
    }

    @Test
    @Transactional
    public void checkVerificationCodeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = verificationCodeRepository.findAll().size();
        // set the field null
        verificationCode.setVerificationCodeId(null);

        // Create the VerificationCode, which fails.
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);

        restVerificationCodeMockMvc.perform(post("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isBadRequest());

        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystemIsRequired() throws Exception {
        int databaseSizeBeforeTest = verificationCodeRepository.findAll().size();
        // set the field null
        verificationCode.setSystem(null);

        // Create the VerificationCode, which fails.
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);

        restVerificationCodeMockMvc.perform(post("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isBadRequest());

        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = verificationCodeRepository.findAll().size();
        // set the field null
        verificationCode.setValue(null);

        // Create the VerificationCode, which fails.
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);

        restVerificationCodeMockMvc.perform(post("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isBadRequest());

        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = verificationCodeRepository.findAll().size();
        // set the field null
        verificationCode.setCode(null);

        // Create the VerificationCode, which fails.
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);

        restVerificationCodeMockMvc.perform(post("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isBadRequest());

        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVerificationCodes() throws Exception {
        // Initialize the database
        verificationCodeRepository.saveAndFlush(verificationCode);

        // Get all the verificationCodes
        restVerificationCodeMockMvc.perform(get("/api/verification-codes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(verificationCode.getId().intValue())))
                .andExpect(jsonPath("$.[*].verificationCodeId").value(hasItem(DEFAULT_VERIFICATION_CODE_ID.toString())))
                .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT_STR)));
    }

    @Test
    @Transactional
    public void getVerificationCode() throws Exception {
        // Initialize the database
        verificationCodeRepository.saveAndFlush(verificationCode);

        // Get the verificationCode
        restVerificationCodeMockMvc.perform(get("/api/verification-codes/{id}", verificationCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(verificationCode.getId().intValue()))
            .andExpect(jsonPath("$.verificationCodeId").value(DEFAULT_VERIFICATION_CODE_ID.toString()))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.verifiedAt").value(DEFAULT_VERIFIED_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingVerificationCode() throws Exception {
        // Get the verificationCode
        restVerificationCodeMockMvc.perform(get("/api/verification-codes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerificationCode() throws Exception {
        // Initialize the database
        verificationCodeRepository.saveAndFlush(verificationCode);
        int databaseSizeBeforeUpdate = verificationCodeRepository.findAll().size();

        // Update the verificationCode
        VerificationCode updatedVerificationCode = new VerificationCode();
        updatedVerificationCode.setId(verificationCode.getId());
        updatedVerificationCode.setVerificationCodeId(UPDATED_VERIFICATION_CODE_ID);
        updatedVerificationCode.setSystem(UPDATED_SYSTEM);
        updatedVerificationCode.setValue(UPDATED_VALUE);
        updatedVerificationCode.setCode(UPDATED_CODE);
        updatedVerificationCode.setVerifiedAt(UPDATED_VERIFIED_AT);
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(updatedVerificationCode);

        restVerificationCodeMockMvc.perform(put("/api/verification-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verificationCodeDTO)))
                .andExpect(status().isOk());

        // Validate the VerificationCode in the database
        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeUpdate);
        VerificationCode testVerificationCode = verificationCodes.get(verificationCodes.size() - 1);
        assertThat(testVerificationCode.getVerificationCodeId()).isEqualTo(UPDATED_VERIFICATION_CODE_ID);
        assertThat(testVerificationCode.getSystem()).isEqualTo(UPDATED_SYSTEM);
        assertThat(testVerificationCode.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testVerificationCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVerificationCode.getVerifiedAt()).isEqualTo(UPDATED_VERIFIED_AT);
    }

    @Test
    @Transactional
    public void deleteVerificationCode() throws Exception {
        // Initialize the database
        verificationCodeRepository.saveAndFlush(verificationCode);
        int databaseSizeBeforeDelete = verificationCodeRepository.findAll().size();

        // Get the verificationCode
        restVerificationCodeMockMvc.perform(delete("/api/verification-codes/{id}", verificationCode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VerificationCode> verificationCodes = verificationCodeRepository.findAll();
        assertThat(verificationCodes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
