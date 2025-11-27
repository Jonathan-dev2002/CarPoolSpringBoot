package com.miniProject.Carpool.dto;

import lombok.Data;

import java.util.List;

@Data
public class VehicleUpdateRequest {
    private String licensePlate;
    private String vehicleModel;
    private String vehicleType;
    private String color;
    private Integer seatCapacity;
    private List<String> amenities;
    private List<String> photos;
    private  Boolean isDefault;
}
