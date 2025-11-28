package com.miniProject.Carpool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingAdminCreateRequest extends BookingRequest {
    @NotBlank(message = "passengerId is required for admin")
    private String passengerId;
}