package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.BookingStatus;
import lombok.Data;

@Data
public class BookingUpdateRequest {
    private String routeId;
    private String passengerId;
    private Integer numberOfSeats;
    private Location pickupLocation;
    private Location dropoffLocation;
    private BookingStatus status;
}