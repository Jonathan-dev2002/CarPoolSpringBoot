package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.BookingStatus;
import com.miniProject.Carpool.model.CancelReason;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private String id;
    private String routeId;

    // Nested objects (Simplified)
    private RouteResponse route;
    private UserResponse passenger;

    private Integer numberOfSeats;
    private BookingStatus status;
    private Location pickupLocation;
    private Location dropoffLocation;

    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private CancelReason cancelReason;

    private LocalDateTime createdAt;
}