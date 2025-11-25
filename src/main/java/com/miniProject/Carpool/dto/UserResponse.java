package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private Role role;
    private Boolean isVerified;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
