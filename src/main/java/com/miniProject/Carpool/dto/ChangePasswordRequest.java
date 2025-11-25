package com.miniProject.Carpool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;

    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    private String confirmNewPassword; // เอาไว้ validate ฝั่ง frontend หรือ controller ก็ได้
}
