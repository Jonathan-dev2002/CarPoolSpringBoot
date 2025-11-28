package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class NotificationCreateRequest {
    @NotBlank(message = "userId is required")
    private String userId;

    private NotificationType type = NotificationType.SYSTEM; // Default

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "body is required")
    private String body;

    private String link;
    private Map<String, Object> metadata;
}