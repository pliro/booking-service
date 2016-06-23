package io.aurora.ams.repository;

import io.aurora.ams.domain.Booking;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@SuppressWarnings("unused")
public interface BookingRepository extends JpaRepository<Booking,Long> {

}
