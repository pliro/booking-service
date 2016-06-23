package io.aurora.ams.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * A DTO for the Payment entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentId;

    private ZonedDateTime paidAt;

    private BigDecimal amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public ZonedDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(ZonedDateTime paidAt) {
        this.paidAt = paidAt;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;

        if ( ! Objects.equals(id, paymentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + id +
            ", paymentId='" + paymentId + "'" +
            ", paidAt='" + paidAt + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
