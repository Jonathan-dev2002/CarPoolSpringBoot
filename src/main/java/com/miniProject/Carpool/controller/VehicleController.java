package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.*;
import com.miniProject.Carpool.dto.search.DriverVerificationSearchRequest;
import com.miniProject.Carpool.dto.search.VehicleSearchRequest;
import com.miniProject.Carpool.model.Vehicle;
import com.miniProject.Carpool.service.DriverVerificationService;
import com.miniProject.Carpool.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final DriverVerificationService driverVerificationService;
    private final VehicleService vehicleService;

    // -- Public / User-specific Routes ---

    //POST /api/vehicle
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(
            @AuthenticationPrincipal String userId,
            @Valid @RequestPart("data") VehicleRequest request,
            @RequestPart("photos") MultipartFile photos
    ) {
        VehicleResponse newVehicle = vehicleService.createVehicle(userId, request, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Vehicle created successfully.", newVehicle));
    }

    //GET /api/vehicle/me
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<VehicleResponse>>> listMyVehicle(
            @AuthenticationPrincipal String userId,
            @ModelAttribute VehicleSearchRequest searchRequest
    ) {
        Page<VehicleResponse> vehicle = vehicleService.searchVehicle(searchRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "My vehicles", vehicle));
    }

    //GET /api/vehicle/:id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(
            @AuthenticationPrincipal String userId,
            @PathVariable("id") String vehicleId
    ) {
        VehicleResponse vehicle = vehicleService.getVehicleById(vehicleId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle found successfully.", vehicle));
    }

    //PUT /api/vehicle/:id
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable("id") String vehicleId,
            @AuthenticationPrincipal String userId,
            @RequestPart(value = "data", required = false) VehicleUpdateRequest request,
            @RequestPart(value = "photos", required = false) MultipartFile photos
    ) {
        VehicleUpdateRequest safeRequest = request != null ? request : new VehicleUpdateRequest();

        VehicleResponse updatedVehicle = vehicleService.updateVehicle(vehicleId, userId, safeRequest, photos);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle updated successfully", updatedVehicle));
    }

    //PATCH /api/vehicle/:id/default
    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateDefaultVehicle(
            @PathVariable String id,
            @AuthenticationPrincipal String userId,
            @RequestBody VehicleStatusUpdateRequest request
    ) {
        if (request.getIsDefault() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "isDefault is required"));
        }
        VehicleResponse updatedVehicle = vehicleService.updateVehicleStatus(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle updated successfully", updatedVehicle));
    }

    //    DELETE /api/vehicle/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> deleteVehicle(
            @AuthenticationPrincipal String userId,
            @PathVariable("id") String vehicleId
    ) {
        vehicleService.deleteMyVehicle(userId, vehicleId);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle deleted", null));
    }

    // --- Admin Routes ---
    //POST /api/vehicle/admin
    @PostMapping(value = "/admin", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicleByAdmin(
            @RequestPart String userId,
            @Valid @RequestPart("data") VehicleRequest request,
            @RequestPart("photos") MultipartFile photos
    ) {
        VehicleResponse newVehicle = vehicleService.createVehicle(userId, request, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Vehicle created successfully.", newVehicle));
    }

    //PUT /api/vehicle/:id
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicleByAdmin(
            @PathVariable("id") String vehicleId,
//            @RequestPart String userId,
            @RequestPart(value = "data", required = false) VehicleUpdateRequest request,
            @RequestPart(value = "photos", required = false) MultipartFile photos
    ) {
        VehicleUpdateRequest safeRequest = request != null ? request : new VehicleUpdateRequest();

        VehicleResponse updatedVehicle = vehicleService.updateVehicleByAdmin(vehicleId, safeRequest, photos);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle updated successfully", updatedVehicle));
    }

    //GET /api/vehicle/admin/:id
    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleByAdmin(
            @PathVariable("id") String vehicleId
    ) {
        VehicleResponse vehicle = vehicleService.getVehicleByAdmin(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle found successfully.", vehicle));
    }

    // GET /api/vehicle/admin (List vehicle)
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<VehicleResponse>>> adminListVehicles(
            @ModelAttribute VehicleSearchRequest request
    ) {
        Page<VehicleResponse> vehicles = vehicleService.searchVehicle(request,null);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle (admin) retrieved", vehicles));
    }

    //    DELETE /api/vehicle/admin/:id
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> deleteVehicleByAdmin(
            @PathVariable("id") String vehicleId
    ) {
        vehicleService.deleteVehicleByAdmin(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(200, "Vehicle deleted", null));
    }
}