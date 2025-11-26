package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.DriverVerificationResponse;
import com.miniProject.Carpool.service.DriverVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/driverVerfication")
@RequiredArgsConstructor
public class DriverVerification {

    private  final DriverVerificationService driverVerificationService;

    // --- Public / User-specific Routes ---

    //POST /api/driverVerification
    @PostMapping(consumes =  {"multipart/form-data"})
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> createDriverVerification(
            @AuthenticationPrincipal String driverId,
            @Valid @RequestPart("data") DriverVerificationResponse request,
            @RequestPart("licensePhotoUrl") MultipartFile licensePhotoUrl,
            @RequestPart("selfiePhotoUrl")MultipartFile selfiePhotoUrl
            ){
        DriverVerificationResponse newDv = driverVerificationService.createDriverVerification()
    }
}

