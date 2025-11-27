package com.miniProject.Carpool.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleSearchRequest extends BaseSearchRequest{
    private String licensePlate;
    private String vehicleType;
    private Integer seatCapacity;

    private String createdFrom;
    private String createdTo;
}
