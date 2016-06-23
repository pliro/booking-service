package io.aurora.ams.service.impl;

import io.aurora.ams.service.VerificationCodeService;
import io.aurora.ams.domain.VerificationCode;
import io.aurora.ams.repository.VerificationCodeRepository;
import io.aurora.ams.web.rest.dto.VerificationCodeDTO;
import io.aurora.ams.web.rest.mapper.VerificationCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing VerificationCode.
 */
@Service
@Transactional
public class VerificationCodeServiceImpl implements VerificationCodeService{

    private final Logger log = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);
    
    @Inject
    private VerificationCodeRepository verificationCodeRepository;
    
    @Inject
    private VerificationCodeMapper verificationCodeMapper;
    
    /**
     * Save a verificationCode.
     * 
     * @param verificationCodeDTO the entity to save
     * @return the persisted entity
     */
    public VerificationCodeDTO save(VerificationCodeDTO verificationCodeDTO) {
        log.debug("Request to save VerificationCode : {}", verificationCodeDTO);
        VerificationCode verificationCode = verificationCodeMapper.verificationCodeDTOToVerificationCode(verificationCodeDTO);
        verificationCode = verificationCodeRepository.save(verificationCode);
        VerificationCodeDTO result = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);
        return result;
    }

    /**
     *  Get all the verificationCodes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<VerificationCode> findAll(Pageable pageable) {
        log.debug("Request to get all VerificationCodes");
        Page<VerificationCode> result = verificationCodeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one verificationCode by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public VerificationCodeDTO findOne(Long id) {
        log.debug("Request to get VerificationCode : {}", id);
        VerificationCode verificationCode = verificationCodeRepository.findOne(id);
        VerificationCodeDTO verificationCodeDTO = verificationCodeMapper.verificationCodeToVerificationCodeDTO(verificationCode);
        return verificationCodeDTO;
    }

    /**
     *  Delete the  verificationCode by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete VerificationCode : {}", id);
        verificationCodeRepository.delete(id);
    }
}
