package com.miniProject.Carpool.dto;

import com.miniProject.Carpool.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegisterRequest {
    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 10, message = "Phone number is required")
    private String phoneNumber;

    @NotBlank
    private String gender;

    @Size(min = 13, max = 13, message = "National ID must be 13 digits")
    private String nationalIdNumber;

    private LocalDateTime nationalIdExpiryDate; // Spring จะแปลง String ISO8601 ให้เอง

    private Role role;

    // รูปภาพจะรับแยกใน Controller ผ่าน @RequestPart หรือใช้ Setter ทีหลัง
}