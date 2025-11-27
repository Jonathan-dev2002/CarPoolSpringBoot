package com.miniProject.Carpool.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VehicleRequest {
    @NotNull(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Vehicle model is required")
    private String vehicleModel;

    @NotNull(message = "Vehicle type is required")
    private String vehicleType;

    @NotNull(message = "Color is required")
    private String color;

    @NotNull(message = "Seat capacity is required")
    private Integer seatCapacity;

//    @NotNull(message = "Amenities is required")
    private List<String> amenities;

    @NotNull(message = "Photos is required")
    private List<String> photos;

    private  Boolean isDefault;
}
