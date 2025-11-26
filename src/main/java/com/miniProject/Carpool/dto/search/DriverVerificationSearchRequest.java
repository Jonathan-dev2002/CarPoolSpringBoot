package com.miniProject.Carpool.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverVerificationSearchRequest extends BaseSearchRequest {
    private String verificationStatus;
    private String typeOnLicense;

    private String createdFrom;
    private String createdTo;
}