package io.aurora.ams.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import io.aurora.ams.domain.enumeration.BookingState;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private String bookingId;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private BookingState state;

    @NotNull
    @Column(name = "slot_id", nullable = false)
    private String slotId;

    @NotNull
    @Column(name = "booked_from", nullable = false)
    private ZonedDateTime bookedFrom;

    @NotNull
    @Column(name = "booked_to", nullable = false)
    private ZonedDateTime bookedTo;

    @NotNull
    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "notes")
    private String notes;

    @Column(name = "person_id")
    private String personId;

    @Column(name = "verification_code_id")
    private String verificationCodeId;

    @Column(name = "payment_id")
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

    public Boolean isActive() {
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
        Booking booking = (Booking) o;
        if(booking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
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
