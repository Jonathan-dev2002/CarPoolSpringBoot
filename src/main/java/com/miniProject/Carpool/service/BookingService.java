package com.miniProject.Carpool.service;

import com.miniProject.Carpool.dto.*;
import com.miniProject.Carpool.dto.search.BookingSearchRequest;
import com.miniProject.Carpool.mapper.BookingMapper;
import com.miniProject.Carpool.model.*;
import com.miniProject.Carpool.repository.BookingRepository;
import com.miniProject.Carpool.repository.NotificationRepository;
import com.miniProject.Carpool.repository.RouteRepository;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.spec.BookingSpecification;
import com.miniProject.Carpool.util.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final BookingMapper bookingMapper;

    public Page<BookingResponse> searchBookings(BookingSearchRequest request) {
        int pageNo = Math.max(0, request.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNo, request.getLimit(), Sort.by("createdAt").descending());

        Specification<Booking> spec = Specification.where(BookingSpecification.hasKeyword(request.getQ()))
                .and(BookingSpecification.hasStatus(request.getStatus()))
                .and(BookingSpecification.hasRouteId(request.getRouteId()))
                .and(BookingSpecification.hasPassengerId(request.getPassengerId()))
                .and(BookingSpecification.hasDriverId(request.getDriverId()))
                .and(BookingSpecification.createdBetween(request.getCreatedFrom(), request.getCreatedTo()));

        return bookingRepository.findAll(spec, pageable).map(bookingMapper::toResponse);
    }

    public BookingResponse getBookingById(String id, String userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "Booking not found"));

        // Allow if passenger OR driver (owner of route)
        boolean isPassenger = booking.getPassenger().getId().equals(userId);
        boolean isDriver = booking.getRoute().getDriverId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new ApiError(403, "Forbidden");
        }
        return bookingMapper.toResponse(booking);
    }

    public BookingResponse getBookingByAdmin(String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "Booking not found"));
        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String passengerId) {
        User passenger = userRepository.findById(passengerId).orElseThrow(() -> new ApiError(404, "User not found"));
        return processCreateBooking(request, passenger);
    }

    @Transactional
    public BookingResponse createBookingByAdmin(BookingAdminCreateRequest request) {
        User passenger = userRepository.findById(request.getPassengerId()).orElseThrow(() -> new ApiError(404, "User not found"));
        return processCreateBooking(request, passenger);
    }

    private BookingResponse processCreateBooking(BookingRequest request, User passenger) {
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ApiError(404, "Route not found"));

        if (route.getDriverId().equals(passenger.getId())) {
            throw new ApiError(400, "Driver cannot book their own route.");
        }
        if (route.getStatus() != RouteStatus.AVAILABLE) {
            throw new ApiError(400, "This route is no longer available.");
        }
        if (route.getAvailableSeats() < request.getNumberOfSeats()) {
            throw new ApiError(400, "Not enough seats available on this route.");
        }

        Booking booking = Booking.builder()
                .route(route)
                .passenger(passenger)
                .numberOfSeats(request.getNumberOfSeats())
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .status(BookingStatus.PENDING)
                .build();

        booking = bookingRepository.save(booking);

        route.setAvailableSeats(route.getAvailableSeats() - request.getNumberOfSeats());
        if (route.getAvailableSeats() == 0) {
            route.setStatus(RouteStatus.FULL);
        }
        routeRepository.save(route);

        createNotification(route.getDriverId(),
                "มีการจองใหม่ในเส้นทางของคุณ",
                "ผู้โดยสารได้ทำการจองที่นั่งในเส้นทางของคุณแล้ว",
                Map.of("kind", "BOOKING_CREATED", "bookingId", booking.getId(), "routeId", route.getId()));

        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingResponse updateBookingStatus(String id, BookingStatus status, String driverId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiError(404, "Booking not found"));

        if (!booking.getRoute().getDriverId().equals(driverId)) {
            throw new ApiError(403, "Forbidden: You are not the driver of this route");
        }

        booking.setStatus(status);
        bookingRepository.save(booking);

        if (status == BookingStatus.REJECTED) {
            refundSeats(booking.getRoute(), booking.getNumberOfSeats());

            createNotification(booking.getPassenger().getId(), "คำขอจองถูกปฏิเสธ",
                    "ขออภัย คนขับได้ปฏิเสธคำขอจองของคุณ",
                    Map.of("kind", "BOOKING_STATUS", "bookingId", id, "status", "REJECTED"));
        } else if (status == BookingStatus.CONFIRMED) {
            createNotification(booking.getPassenger().getId(), "คำขอจองได้รับการยืนยัน",
                    "คนขับได้ยืนยันการจองของคุณแล้ว",
                    Map.of("kind", "BOOKING_STATUS", "bookingId", id, "status", "CONFIRMED"));
        }

        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(String id, String passengerId, BookingCancelRequest request) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiError(404, "Booking not found"));

        if (!booking.getPassenger().getId().equals(passengerId)) {
            throw new ApiError(403, "Forbidden");
        }
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ApiError(400, "Cannot cancel at this stage");
        }

        boolean wasConfirmed = (booking.getStatus() == BookingStatus.CONFIRMED);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelledBy("PASSENGER");
        booking.setCancelReason(request.getReason());

        bookingRepository.save(booking);

        refundSeats(booking.getRoute(), booking.getNumberOfSeats());

        if (wasConfirmed) {
            createNotification(passengerId, "บันทึกการยกเลิกหลังยืนยัน",
                    "คุณได้ยกเลิกการจองที่เคยได้รับการยืนยันแล้ว",
                    Map.of("kind", "PASSENGER_CONFIRMED_CANCEL", "bookingId", id));
        }

        return bookingMapper.toResponse(booking);
    }

    public void deleteBooking(String id, String userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiError(404, "Booking not found"));

        if (booking.getStatus() != BookingStatus.CANCELLED && booking.getStatus() != BookingStatus.REJECTED) {
            throw new ApiError(400, "Only cancelled or rejected bookings can be deleted");
        }

        boolean isPassenger = booking.getPassenger().getId().equals(userId);
        boolean isDriver = booking.getRoute().getDriverId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new ApiError(403, "Forbidden");
        }
        bookingRepository.delete(booking);
    }

    @Transactional
    public void deleteBookingByAdmin(String id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiError(404, "Booking not found"));

        if (booking.getStatus() == BookingStatus.PENDING || booking.getStatus() == BookingStatus.CONFIRMED) {
            refundSeats(booking.getRoute(), booking.getNumberOfSeats());
        }
        bookingRepository.delete(booking);
    }

    private void refundSeats(Route route, int seatsToRefund) {
        route.setAvailableSeats(route.getAvailableSeats() + seatsToRefund);
        if (route.getStatus() == RouteStatus.FULL && route.getAvailableSeats() > 0) {
            route.setStatus(RouteStatus.AVAILABLE);
        }
        routeRepository.save(route);
    }

    private void createNotification(String userId, String title, String body, Map<String, Object> metadata) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Notification n = Notification.builder()
                    .user(user)
                    .type(NotificationType.BOOKING) // หรือรับ type มาก็ได้
                    .title(title)
                    .body(body)
                    .metadata(metadata)
                    .build();
            notificationRepository.save(n);
        }
    }
}