package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.DriverVerificationRequest;
import com.miniProject.Carpool.dto.DriverVerificationResponse;
import com.miniProject.Carpool.dto.DriverVerificationStatusUpdateRequest;
import com.miniProject.Carpool.dto.search.DriverVerificationSearchRequest;
import com.miniProject.Carpool.model.DriverVerification;
import com.miniProject.Carpool.service.DriverVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/driverVerification")
@RequiredArgsConstructor
public class DriverVerificationController {

    private final DriverVerificationService driverVerificationService;

    // --- Public / User-specific Routes ---

    //POST /api/driverVerification
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> createDriverVerification(
            @AuthenticationPrincipal String userId,
            @Valid @RequestPart("data") DriverVerificationRequest request,
            @RequestPart("licensePhotoUrl") MultipartFile licensePhotoUrl,
            @RequestPart("selfiePhotoUrl") MultipartFile selfiePhotoUrl
    ) {
        DriverVerificationResponse newDv = driverVerificationService.createDriverVerification(userId, request, licensePhotoUrl, selfiePhotoUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Dv created successfully. Please wait for verification.", newDv));
    }
    //GET /api/driverVerification/me
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> getDriverVerificationByUser(@AuthenticationPrincipal String userId) {
        DriverVerificationResponse driverVerification = driverVerificationService.getDriverVerificationById(userId);
        return ResponseEntity.ok(ApiResponse.success(200,"Driver Verification",driverVerification));
    }

    // --- Admin Routes ---
    // PATCH /api/driverVerification/admin/:id/status
    @PatchMapping("/admin/{id}/status")
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> setDriverVerificationStatus(
            @PathVariable String id,
            @RequestBody DriverVerificationStatusUpdateRequest request) {
        if (request.getVerificationStatus() == null) {
            return ResponseEntity.badRequest().build();
        }
        DriverVerificationResponse updatedDv = driverVerificationService.updateDriverVerificationStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "DriverVerification status updated", updatedDv));
    }

    // DELETE /api/driverVerification/admin/:id
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> deleteDriverVerification(
            @PathVariable String id
    ) {
        driverVerificationService.deleteDriverVerificationById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "DriverVerification deleted", null));
    }

    // GET /api/driverVerification/admin (List DriverVerification)
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<DriverVerificationResponse>>> adminListDriverVerification(
            @ModelAttribute DriverVerificationSearchRequest searchRequest
    ) {
        Page<DriverVerificationResponse> driverVerifications = driverVerificationService.searchDriverVerifications(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(200, "DriverVerification (admin) retrieved", driverVerifications));
    }

    //GET /api/driverVerification/admin/:id
    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<DriverVerificationResponse>> getDriverVerificationById(@PathVariable String id) {
        DriverVerificationResponse driverVerification = driverVerificationService.getDriverVerificationById(id);
        return ResponseEntity.ok(ApiResponse.success(200,"Driver Verification",driverVerification));
    }

}

