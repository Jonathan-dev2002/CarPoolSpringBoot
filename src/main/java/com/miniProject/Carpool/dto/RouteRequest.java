package com.miniProject.Carpool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miniProject.Carpool.model.RouteStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RouteRequest {
    @NotNull
    private String vehicleId;

    @NotNull
    private Location startLocation;

    @NotNull
    private Location endLocation;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // หรือ ISO format แล้วแต่ Frontend ส่ง
    private LocalDateTime departureTime;

    @NotNull
    private Integer availableSeats;

    @NotNull
    private Double pricePerSeat;

    private String conditions;

    // Google Maps options
    private List<String> waypoints; // ส่งเป็น address string หรือ lat,lng
    private Boolean optimizeWaypoints;

    // Admin Only
    private String driverId;
    private RouteStatus status;
}