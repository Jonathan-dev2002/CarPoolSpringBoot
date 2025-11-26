package com.miniProject.Carpool.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @UuidGenerator
    private String id;

    private String driverId;
    private String vehicleId;

    @Column(columnDefinition = "TEXT")
    private String startLocation; // JSON String

    @Column(columnDefinition = "TEXT")
    private String endLocation;   // JSON String

    private LocalDateTime departureTime;
    private Integer availableSeats;
    private Double pricePerSeat;
    private String conditions;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RouteStatus status = RouteStatus.AVAILABLE;

    private LocalDateTime cancelledAt;
    private String cancelledBy;

    // Google Maps Data
    @Column(columnDefinition = "TEXT")
    private String routePolyline;

    private Integer distanceMeters;
    private Integer durationSeconds;
    private String routeSummary;
    private String distance;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String waypoints;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}