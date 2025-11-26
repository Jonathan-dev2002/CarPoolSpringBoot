package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.RouteStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RouteResponse {
    private String id;
    private String driverId;
    private String vehicleId;

    // เปลี่ยนจาก String JSON เป็น Object Location
    private Location startLocation;
    private Location endLocation;

    private LocalDateTime departureTime;
    private Integer availableSeats;
    private Double pricePerSeat;
    private String conditions;
    private RouteStatus status;

    // Google Maps Info
    private String routeSummary;
    private String distance; // เช่น "15 km"
    private String duration; // เช่น "20 mins"
    private String routePolyline; // เส้นที่วาดบนแผนที่
}