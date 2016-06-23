package io.aurora.ams.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import io.aurora.ams.domain.enumeration.ContactPointSystem;

/**
 * A DTO for the VerificationCode entity.
 */
public class VerificationCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String verificationCodeId;

    @NotNull
    private ContactPointSystem system;

    @NotNull
    private String value;

    @NotNull
    private String code;

    private ZonedDateTime verifiedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getVerificationCodeId() {
        return verificationCodeId;
    }

    public void setVerificationCodeId(String verificationCodeId) {
        this.verificationCodeId = verificationCodeId;
    }
    public ContactPointSystem getSystem() {
        return system;
    }

    public void setSystem(ContactPointSystem system) {
        this.system = system;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public ZonedDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(ZonedDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VerificationCodeDTO verificationCodeDTO = (VerificationCodeDTO) o;

        if ( ! Objects.equals(id, verificationCodeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VerificationCodeDTO{" +
            "id=" + id +
            ", verificationCodeId='" + verificationCodeId + "'" +
            ", system='" + system + "'" +
            ", value='" + value + "'" +
            ", code='" + code + "'" +
            ", verifiedAt='" + verifiedAt + "'" +
            '}';
    }
}
