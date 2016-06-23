package io.aurora.ams.service;

import io.aurora.ams.domain.VerificationCode;
import io.aurora.ams.web.rest.dto.VerificationCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing VerificationCode.
 */
public interface VerificationCodeService {

    /**
     * Save a verificationCode.
     * 
     * @param verificationCodeDTO the entity to save
     * @return the persisted entity
     */
    VerificationCodeDTO save(VerificationCodeDTO verificationCodeDTO);

    /**
     *  Get all the verificationCodes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<VerificationCode> findAll(Pageable pageable);

    /**
     *  Get the "id" verificationCode.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    VerificationCodeDTO findOne(Long id);

    /**
     *  Delete the "id" verificationCode.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
