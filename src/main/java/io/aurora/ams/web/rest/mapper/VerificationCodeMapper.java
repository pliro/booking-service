package io.aurora.ams.web.rest.mapper;

import io.aurora.ams.domain.*;
import io.aurora.ams.web.rest.dto.VerificationCodeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity VerificationCode and its DTO VerificationCodeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VerificationCodeMapper {

    VerificationCodeDTO verificationCodeToVerificationCodeDTO(VerificationCode verificationCode);

    List<VerificationCodeDTO> verificationCodesToVerificationCodeDTOs(List<VerificationCode> verificationCodes);

    VerificationCode verificationCodeDTOToVerificationCode(VerificationCodeDTO verificationCodeDTO);

    List<VerificationCode> verificationCodeDTOsToVerificationCodes(List<VerificationCodeDTO> verificationCodeDTOs);
}
