package io.aurora.ams.service;

import io.aurora.ams.domain.Payment;
import io.aurora.ams.web.rest.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Payment.
 */
public interface PaymentService {

    /**
     * Save a payment.
     * 
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    PaymentDTO save(PaymentDTO paymentDTO);

    /**
     *  Get all the payments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Payment> findAll(Pageable pageable);

    /**
     *  Get the "id" payment.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PaymentDTO findOne(Long id);

    /**
     *  Delete the "id" payment.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
