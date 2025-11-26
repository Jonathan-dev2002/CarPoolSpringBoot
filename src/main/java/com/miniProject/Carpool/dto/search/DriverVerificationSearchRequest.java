package com.miniProject.Carpool.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverVerificationRequest extends BaseSearchRequest {
    private String verificationStatus;
    private String typeOnLicense;
}
