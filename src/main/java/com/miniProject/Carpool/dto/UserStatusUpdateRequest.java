package com.miniProject.Carpool.dto;

import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    private Boolean isActive;
    private Boolean isVerified;
}
