package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.*;
import com.miniProject.Carpool.dto.search.UserSearchRequest;
import com.miniProject.Carpool.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- Public / User-specific Routes ---

    // POST /api/users (Register)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestPart("data") UserRegisterRequest request,
            @RequestPart("nationalIdPhotoUrl") MultipartFile nationalIdPhoto,
            @RequestPart("selfiePhotoUrl") MultipartFile selfiePhoto
    ) {
        UserResponse newUser = userService.createUser(request, nationalIdPhoto, selfiePhoto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "User created successfully. Please wait for verification.", newUser));
    }

    // GET /api/users/me (Get My Profile)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyUser(
            @AuthenticationPrincipal String userId // รับ ID จาก JWT
    ) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(200, "User retrieved", user));
    }

    // PUT /api/users/me (Update My Profile)
    @PutMapping(value = "/me", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUserProfile(
            @AuthenticationPrincipal String userId,
            @RequestPart(value = "data", required = false) UserUpdateRequest request, // JSON อาจจะไม่ส่งก็ได้ถ้าแก้แค่รูป
            @RequestPart(value = "nationalIdPhotoUrl", required = false) MultipartFile nationalIdPhoto,
            @RequestPart(value = "selfiePhotoUrl", required = false) MultipartFile selfiePhoto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        // ถ้า request เป็น null (แก้แค่รูป) ให้สร้าง object เปล่า
        UserUpdateRequest safeRequest = request != null ? request : new UserUpdateRequest();

        // ป้องกัน User แก้ Role/Status เอง (Set null ทับไปเลย)
        safeRequest.setRole(null);
        safeRequest.setIsActive(null);
        safeRequest.setIsVerified(null);

        UserResponse updatedUser = userService.updateUser(userId, safeRequest, nationalIdPhoto, selfiePhoto, profilePicture);
        return ResponseEntity.ok(ApiResponse.success(200, "Profile updated", updatedUser));
    }

    // GET /api/users/:id (Public Profile)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserPublicById(@PathVariable String id) {
        UserResponse user = userService.getUserPublicById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "User retrieved", user));
    }


    // --- Admin Routes ---

    // GET /api/users/admin (List Users)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> adminListUsers(
            @ModelAttribute UserSearchRequest searchRequest
    ) {
        Page<UserResponse> users = userService.searchUsers(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(200, "Users (admin) retrieved", users));
    }

    // GET /api/users/admin/:id (Get Full Profile by Admin)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "User retrieved", user));
    }

    // PUT /api/users/admin/:id (Update User by Admin)
    @PutMapping(value = "/admin/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> adminUpdateUser(
            @PathVariable String id,
            @RequestPart(value = "data", required = false) UserUpdateRequest request,
            @RequestPart(value = "nationalIdPhotoUrl", required = false) MultipartFile nationalIdPhoto,
            @RequestPart(value = "selfiePhotoUrl", required = false) MultipartFile selfiePhoto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        UserUpdateRequest safeRequest = request != null ? request : new UserUpdateRequest();
        UserResponse updatedUser = userService.updateUser(id, safeRequest, nationalIdPhoto, selfiePhoto, profilePicture);
        return ResponseEntity.ok(ApiResponse.success(200, "User updated by admin", updatedUser));
    }

    // PATCH /api/users/admin/:id/status (Set Status)
    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> setUserStatus(
            @PathVariable String id,
            @RequestBody UserStatusUpdateRequest request
    ) {
        if (request.getIsActive() == null && request.getIsVerified() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Provide at least one of isActive or isVerified"));
        }
        UserResponse updatedUser = userService.updateUserStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "User status updated", updatedUser));
    }

    // DELETE /api/users/admin/:id
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> adminDeleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(200, "User deleted successfully", null));
    }
}