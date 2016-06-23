package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.VerificationCode;
import io.aurora.ams.service.VerificationCodeService;
import io.aurora.ams.web.rest.util.HeaderUtil;
import io.aurora.ams.web.rest.util.PaginationUtil;
import io.aurora.ams.web.rest.dto.VerificationCodeDTO;
import io.aurora.ams.web.rest.mapper.VerificationCodeMapper;
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
 * REST controller for managing VerificationCode.
 */
@RestController
@RequestMapping("/api")
public class VerificationCodeResource {

    private final Logger log = LoggerFactory.getLogger(VerificationCodeResource.class);
        
    @Inject
    private VerificationCodeService verificationCodeService;
    
    @Inject
    private VerificationCodeMapper verificationCodeMapper;
    
    /**
     * POST  /verification-codes : Create a new verificationCode.
     *
     * @param verificationCodeDTO the verificationCodeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new verificationCodeDTO, or with status 400 (Bad Request) if the verificationCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/verification-codes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VerificationCodeDTO> createVerificationCode(@Valid @RequestBody VerificationCodeDTO verificationCodeDTO) throws URISyntaxException {
        log.debug("REST request to save VerificationCode : {}", verificationCodeDTO);
        if (verificationCodeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("verificationCode", "idexists", "A new verificationCode cannot already have an ID")).body(null);
        }
        VerificationCodeDTO result = verificationCodeService.save(verificationCodeDTO);
        return ResponseEntity.created(new URI("/api/verification-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("verificationCode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /verification-codes : Updates an existing verificationCode.
     *
     * @param verificationCodeDTO the verificationCodeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated verificationCodeDTO,
     * or with status 400 (Bad Request) if the verificationCodeDTO is not valid,
     * or with status 500 (Internal Server Error) if the verificationCodeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/verification-codes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VerificationCodeDTO> updateVerificationCode(@Valid @RequestBody VerificationCodeDTO verificationCodeDTO) throws URISyntaxException {
        log.debug("REST request to update VerificationCode : {}", verificationCodeDTO);
        if (verificationCodeDTO.getId() == null) {
            return createVerificationCode(verificationCodeDTO);
        }
        VerificationCodeDTO result = verificationCodeService.save(verificationCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("verificationCode", verificationCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /verification-codes : get all the verificationCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of verificationCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/verification-codes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<VerificationCodeDTO>> getAllVerificationCodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of VerificationCodes");
        Page<VerificationCode> page = verificationCodeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/verification-codes");
        return new ResponseEntity<>(verificationCodeMapper.verificationCodesToVerificationCodeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /verification-codes/:id : get the "id" verificationCode.
     *
     * @param id the id of the verificationCodeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the verificationCodeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/verification-codes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VerificationCodeDTO> getVerificationCode(@PathVariable Long id) {
        log.debug("REST request to get VerificationCode : {}", id);
        VerificationCodeDTO verificationCodeDTO = verificationCodeService.findOne(id);
        return Optional.ofNullable(verificationCodeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /verification-codes/:id : delete the "id" verificationCode.
     *
     * @param id the id of the verificationCodeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/verification-codes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVerificationCode(@PathVariable Long id) {
        log.debug("REST request to delete VerificationCode : {}", id);
        verificationCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("verificationCode", id.toString())).build();
    }

}
