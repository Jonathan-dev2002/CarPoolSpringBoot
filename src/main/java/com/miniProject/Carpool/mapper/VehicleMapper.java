package com.miniProject.Carpool.mapper;

import com.miniProject.Carpool.dto.VehicleResponse;
import com.miniProject.Carpool.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public VehicleResponse toResponse(Vehicle vehicle) {
        if(vehicle ==null) return null;

        VehicleResponse res = new VehicleResponse();
        res.setId(vehicle.getId());
        res.setUserId(vehicle.getUser().getId());
        res.setLicensePlate(vehicle.getLicensePlate());
        res.setVehicleModel(vehicle.getVehicleModel());
        res.setVehicleType(vehicle.getVehicleType());
        res.setColor(vehicle.getColor());
        res.setSeatCapacity(vehicle.getSeatCapacity());
        res.setAmenities(vehicle.getAmenities());
        res.setPhotos(vehicle.getPhotos());
        res.setIsDefault(vehicle.getIsDefault());

        res.setCreatedAt(vehicle.getCreatedAt());
        res.setUpdatedAt(vehicle.getUpdatedAt());

        return res;
    }
}
