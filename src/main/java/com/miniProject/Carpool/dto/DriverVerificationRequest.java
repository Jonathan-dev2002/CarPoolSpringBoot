package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.LicenseType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DriverVerificationRequest {
    @NotNull(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "First name of license is required")
    private String firstNameOnLicense;

    @NotNull(message = "Last name of license is required")
    private String lastNameOnLicense;

    @NotNull(message = "License type is required")
    private LicenseType typeOnLicense;

    @NotNull(message = "License issue date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime licenseIssueDate;

    @NotNull(message = "License expiry date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime licenseExpiryDate;
}
