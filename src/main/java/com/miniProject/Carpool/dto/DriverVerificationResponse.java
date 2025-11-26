package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.VerificationStatus;
import com.miniProject.Carpool.model.LicenseType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DriverVerificationResponse {
    private String id;
    private String userId;
    private String licenseNumber;
    private String firstNameOnLicense;
    private String lastNameOnLicense;

    private LocalDateTime licenseExpiryDate;
    private LocalDateTime licenseIssueDate;

    private String licensePhotoUrl;
    private String selfiePhotoUrl;

    private LicenseType licenseType;
    private VerificationStatus verificationStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
