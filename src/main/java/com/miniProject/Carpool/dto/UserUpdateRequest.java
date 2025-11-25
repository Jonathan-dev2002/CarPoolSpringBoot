package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.Role;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;

    //Admin
    private Role role;
    private Boolean isActive;
    private Boolean isVerified;
}