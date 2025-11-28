package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.*;
import com.miniProject.Carpool.dto.search.BookingSearchRequest;
import com.miniProject.Carpool.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // --- User Routes ---

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> listMyBookings(
            @AuthenticationPrincipal String userId,
            @ModelAttribute BookingSearchRequest request
    ) {
        // Force passengerId to be current user
        request.setPassengerId(userId);
        Page<BookingResponse> result = bookingService.searchBookings(request);
        return ResponseEntity.ok(ApiResponse.success(200, "My bookings", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            @AuthenticationPrincipal String userId,
            @PathVariable("id") String id
    ) {
        BookingResponse result = bookingService.getBookingById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking retrieved", result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody BookingRequest request
    ) {
        BookingResponse created = bookingService.createBooking(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Booking created successfully", created));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(
            @AuthenticationPrincipal String userId, // Driver ID
            @PathVariable("id") String id,
            @Valid @RequestBody BookingStatusUpdateRequest request
    ) {
        BookingResponse updated = bookingService.updateBookingStatus(id, request.getStatus(), userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking status updated", updated));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable("id") String id,
            @Valid @RequestBody BookingCancelRequest request
    ) {
        BookingResponse cancelled = bookingService.cancelBooking(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking cancelled", cancelled));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable("id") String id
    ) {
        bookingService.deleteBooking(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking deleted", null));
    }

    // --- Admin Routes ---

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> adminListBookings(
            @ModelAttribute BookingSearchRequest request
    ) {
        Page<BookingResponse> result = bookingService.searchBookings(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Bookings (admin) retrieved", result));
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> adminGetBookingById(
            @PathVariable("id") String id
    ) {
        BookingResponse result = bookingService.getBookingByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking (admin) retrieved", result));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> adminCreateBooking(
            @Valid @RequestBody BookingAdminCreateRequest request
    ) {
        BookingResponse created = bookingService.createBookingByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Booking created by admin", created));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> adminDeleteBooking(
            @PathVariable("id") String id
    ) {
        bookingService.deleteBookingByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking deleted by admin", null));
    }
}