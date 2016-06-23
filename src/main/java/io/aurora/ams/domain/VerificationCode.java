package io.aurora.ams.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import io.aurora.ams.domain.enumeration.ContactPointSystem;

/**
 * A VerificationCode.
 */
@Entity
@Table(name = "verification_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "verification_code_id", nullable = false)
    private String verificationCodeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "system", nullable = false)
    private ContactPointSystem system;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "verified_at")
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
        VerificationCode verificationCode = (VerificationCode) o;
        if(verificationCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, verificationCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
            "id=" + id +
            ", verificationCodeId='" + verificationCodeId + "'" +
            ", system='" + system + "'" +
            ", value='" + value + "'" +
            ", code='" + code + "'" +
            ", verifiedAt='" + verifiedAt + "'" +
            '}';
    }
}
