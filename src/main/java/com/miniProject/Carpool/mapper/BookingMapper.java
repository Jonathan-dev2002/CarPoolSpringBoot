package com.miniProject.Carpool.mapper;

import com.miniProject.Carpool.dto.BookingResponse;
import com.miniProject.Carpool.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final RouteMapper routeMapper;
    private final UserMapper userMapper;

    public BookingResponse toResponse(Booking booking) {
        if (booking == null) return null;

        return BookingResponse.builder()
                .id(booking.getId())
                .routeId(booking.getRoute().getId())
                .route(routeMapper.toResponse(booking.getRoute())) // Reuse RouteMapper
                .passenger(userMapper.toPublicResponse(booking.getPassenger())) // Reuse UserMapper (Public info)
                .numberOfSeats(booking.getNumberOfSeats())
                .status(booking.getStatus())
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .cancelledAt(booking.getCancelledAt())
                .cancelledBy(booking.getCancelledBy())
                .cancelReason(booking.getCancelReason())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}