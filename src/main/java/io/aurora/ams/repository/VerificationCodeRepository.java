package io.aurora.ams.repository;

import io.aurora.ams.domain.VerificationCode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VerificationCode entity.
 */
@SuppressWarnings("unused")
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

}
