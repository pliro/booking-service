package io.aurora.ams.web.rest.mapper;

import io.aurora.ams.domain.*;
import io.aurora.ams.web.rest.dto.BookingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Booking and its DTO BookingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookingMapper {

    BookingDTO bookingToBookingDTO(Booking booking);

    List<BookingDTO> bookingsToBookingDTOs(List<Booking> bookings);

    Booking bookingDTOToBooking(BookingDTO bookingDTO);

    List<Booking> bookingDTOsToBookings(List<BookingDTO> bookingDTOs);
}
