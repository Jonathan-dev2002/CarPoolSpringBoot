package com.miniProject.Carpool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "routeId is required")
    private String routeId;

    @NotNull
    @Min(value = 1, message = "At least 1 seat is required")
    private Integer numberOfSeats;

    @NotNull
    private Location pickupLocation;

    @NotNull
    private Location dropoffLocation;
}