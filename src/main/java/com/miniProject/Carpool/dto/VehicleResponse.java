package com.miniProject.Carpool.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VehicleResponse {
    private String id;
    private String userId;

    private String licensePlate;
    private String vehicleModel;
    private String vehicleType;
    private String color;
    private Integer seatCapacity;
    private List<String> amenities;
    private List<String> photos;

    private Boolean isDefault;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
