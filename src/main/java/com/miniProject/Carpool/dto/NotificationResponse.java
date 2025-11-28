package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private String userId;
    // user info ย่อ สำหรับ admin list
    private UserSubset user;

    private NotificationType type;
    private String title;
    private String body;
    private String link;
    private Map<String, Object> metadata;

    private LocalDateTime readAt;
    private LocalDateTime adminReviewedAt;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class UserSubset {
        private String id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
    }
}