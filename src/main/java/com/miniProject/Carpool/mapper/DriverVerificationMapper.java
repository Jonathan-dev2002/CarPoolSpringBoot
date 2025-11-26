package com.miniProject.Carpool.mapper;

import com.miniProject.Carpool.dto.DriverVerificationResponse;
import com.miniProject.Carpool.model.DriverVerification;
import org.springframework.stereotype.Component;

@Component
public class DriverVerificationMapper {

    public DriverVerificationResponse toResponse(DriverVerification driverVerification) {
        if (driverVerification == null) return null;

        DriverVerificationResponse res = new DriverVerificationResponse();
        res.setId(driverVerification.getId());
        res.setUserId(driverVerification.getUser().getId());
        res.setLicenseNumber(driverVerification.getLicenseNumber());
        res.setFirstNameOnLicense(driverVerification.getFirstNameOnLicense());
        res.setLastNameOnLicense(driverVerification.getLastNameOnLicense());
        res.setLicenseExpiryDate(driverVerification.getLicenseExpiryDate());
        res.setLicenseIssueDate(driverVerification.getLicenseIssueDate());

        res.setLicenseType(driverVerification.getTypeOnLicense());
        res.setVerificationStatus(driverVerification.getVerificationStatus());

        res.setLicenseExpiryDate(driverVerification.getLicenseExpiryDate());
        res.setLicenseIssueDate(driverVerification.getLicenseIssueDate());

        res.setCreatedAt(driverVerification.getCreatedAt());
        res.setUpdatedAt(driverVerification.getUpdatedAt());

        return res;
    }
}
