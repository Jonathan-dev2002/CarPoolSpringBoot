package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.NotificationCreateRequest;
import com.miniProject.Carpool.dto.NotificationResponse;
import com.miniProject.Carpool.dto.search.NotificationSearchRequest;
import com.miniProject.Carpool.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> listMyNotifications(
            @AuthenticationPrincipal String userId,
            @ModelAttribute NotificationSearchRequest request
    ) {
        Page<NotificationResponse> result = notificationService.listNotifications(userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Notifications retrieved successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getMyNotificationById(
            @AuthenticationPrincipal String userId,
            @PathVariable String id
    ) {
        NotificationResponse result = notificationService.getMyNotificationById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification retrieved successfully", result));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markRead(
            @AuthenticationPrincipal String userId,
            @PathVariable String id
    ) {
        NotificationResponse result = notificationService.markRead(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification marked as read", result));
    }

    @PatchMapping("/{id}/unread")
    public ResponseEntity<ApiResponse<NotificationResponse>> markUnread(
            @AuthenticationPrincipal String userId,
            @PathVariable String id
    ) {
        NotificationResponse result = notificationService.markUnread(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification marked as unread", result));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Object>> markAllRead(
            @AuthenticationPrincipal String userId
    ) {
        notificationService.markAllRead(userId);
        return ResponseEntity.ok(ApiResponse.success(200, "All notifications marked as read", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteMyNotification(
            @AuthenticationPrincipal String userId,
            @PathVariable String id
    ) {
        notificationService.deleteMyNotification(id, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification deleted successfully", null));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countUnread(
            @AuthenticationPrincipal String userId
    ) {
        Map<String, Long> count = notificationService.countUnread(userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Unread count retrieved successfully", count));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> listNotificationsAdmin(
            @ModelAttribute NotificationSearchRequest request
    ) {
        Page<NotificationResponse> result = notificationService.listNotifications(null, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Notifications (admin) retrieved successfully", result));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotificationByAdmin(
            @Valid @RequestBody NotificationCreateRequest request
    ) {
        NotificationResponse created = notificationService.createNotificationByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Notification (admin) created successfully", created));
    }

    @PatchMapping("/admin/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> adminMarkRead(
            @PathVariable String id
    ) {
        NotificationResponse result = notificationService.adminMarkRead(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification marked as reviewed", result));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteNotificationByAdmin(
            @PathVariable String id
    ) {
        notificationService.deleteNotificationByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Notification (admin) deleted successfully", null));
    }
}