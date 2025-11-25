package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.ChangePasswordRequest;
import com.miniProject.Carpool.dto.LoginRequest;
import com.miniProject.Carpool.dto.LoginResponse;
import com.miniProject.Carpool.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Login successful", response));
    }

    // PUT /api/auth/change-password
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @AuthenticationPrincipal String userId, // ดึง User ID จาก Token ที่ Filter ใส่ให้
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Password changed successfully", null));
    }
}