package com.miniProject.Carpool.mapper;

import com.miniProject.Carpool.dto.NotificationResponse;
import com.miniProject.Carpool.model.Notification;
import com.miniProject.Carpool.model.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) return null;

        User user = notification.getUser();
        NotificationResponse.UserSubset userSubset = null;
        if (user != null) {
            userSubset = NotificationResponse.UserSubset.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(user != null ? user.getId() : null)
                .user(userSubset)
                .type(notification.getType())
                .title(notification.getTitle())
                .body(notification.getBody())
                .link(notification.getLink())
                .metadata(notification.getMetadata())
                .readAt(notification.getReadAt())
                .adminReviewedAt(notification.getAdminReviewedAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}