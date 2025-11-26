package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.VerificationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
public class DriverVerificationStatusUpdateRequest {
    private VerificationStatus verificationStatus;
}
