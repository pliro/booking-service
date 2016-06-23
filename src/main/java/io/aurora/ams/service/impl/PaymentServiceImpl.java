package io.aurora.ams.service.impl;

import io.aurora.ams.service.PaymentService;
import io.aurora.ams.domain.Payment;
import io.aurora.ams.repository.PaymentRepository;
import io.aurora.ams.web.rest.dto.PaymentDTO;
import io.aurora.ams.web.rest.mapper.PaymentMapper;
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
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    @Inject
    private PaymentRepository paymentRepository;
    
    @Inject
    private PaymentMapper paymentMapper;
    
    /**
     * Save a payment.
     * 
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.paymentToPaymentDTO(payment);
        return result;
    }

    /**
     *  Get all the payments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Payment> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        Page<Payment> result = paymentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PaymentDTO findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        PaymentDTO paymentDTO = paymentMapper.paymentToPaymentDTO(payment);
        return paymentDTO;
    }

    /**
     *  Delete the  payment by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
    }
}
