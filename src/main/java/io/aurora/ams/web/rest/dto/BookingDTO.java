package io.aurora.ams.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import io.aurora.ams.domain.enumeration.BookingState;

/**
 * A DTO for the Booking entity.
 */
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    private String bookingId;

    @NotNull
    private ZonedDateTime expiresAt;

    @NotNull
    private BookingState state;

    @NotNull
    private String slotId;

    @NotNull
    private ZonedDateTime bookedFrom;

    @NotNull
    private ZonedDateTime bookedTo;

    @NotNull
    private String resourceId;

    private String appointmentId;

    private Boolean active;

    private String notes;

    private String personId;

    private String verificationCodeId;

    private String paymentId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
    public ZonedDateTime getBookedFrom() {
        return bookedFrom;
    }

    public void setBookedFrom(ZonedDateTime bookedFrom) {
        this.bookedFrom = bookedFrom;
    }
    public ZonedDateTime getBookedTo() {
        return bookedTo;
    }

    public void setBookedTo(ZonedDateTime bookedTo) {
        this.bookedTo = bookedTo;
    }
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    public String getVerificationCodeId() {
        return verificationCodeId;
    }

    public void setVerificationCodeId(String verificationCodeId) {
        this.verificationCodeId = verificationCodeId;
    }
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;

        if ( ! Objects.equals(id, bookingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + id +
            ", bookingId='" + bookingId + "'" +
            ", expiresAt='" + expiresAt + "'" +
            ", state='" + state + "'" +
            ", slotId='" + slotId + "'" +
            ", bookedFrom='" + bookedFrom + "'" +
            ", bookedTo='" + bookedTo + "'" +
            ", resourceId='" + resourceId + "'" +
            ", appointmentId='" + appointmentId + "'" +
            ", active='" + active + "'" +
            ", notes='" + notes + "'" +
            ", personId='" + personId + "'" +
            ", verificationCodeId='" + verificationCodeId + "'" +
            ", paymentId='" + paymentId + "'" +
            '}';
    }
}
