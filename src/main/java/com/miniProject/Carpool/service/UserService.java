package com.miniProject.Carpool.service;

import com.miniProject.Carpool.dto.UserRegisterRequest;
import com.miniProject.Carpool.dto.UserResponse;
import com.miniProject.Carpool.dto.UserStatusUpdateRequest;
import com.miniProject.Carpool.dto.UserUpdateRequest;
import com.miniProject.Carpool.dto.search.UserSearchRequest;
import com.miniProject.Carpool.mapper.UserMapper;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.spec.UserSpecification;
import com.miniProject.Carpool.util.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    // private final CloudinaryService cloudinaryService;

    public UserResponse createUser(UserRegisterRequest request, MultipartFile nationalIdPhoto, MultipartFile selfiePhoto) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiError(409, "This email is already in use.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiError(409, "This username is already taken.");
        }

        String nationalIdUrl = "mock_url_national_id";
        String selfieUrl = "mock_url_selfie";

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .nationalIdNumber(request.getNationalIdNumber())
                .nationalIdExpiryDate(request.getNationalIdExpiryDate())
                .nationalIdPhotoUrl(nationalIdUrl)
                .selfiePhotoUrl(selfieUrl)
                .role(request.getRole())
                .isActive(true)
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        // TODO: Create Notification Logic Here

        return userMapper.toResponse(savedUser);
    }

    public Page<UserResponse> searchUsers(UserSearchRequest request) {
        //จัดการ Page (Spring เริ่ม 0)
        int pageNo = Math.max(0, request.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNo, request.getLimit(), Sort.by("createdAt").descending());

        Specification<User> spec = Specification.where(UserSpecification.hasKeyword(request.getQ()))
                .and(UserSpecification.hasRole(request.getRole()))
                .and(UserSpecification.hasActiveStatus(request.getIsActive()))
                .and(UserSpecification.hasVerifiedStatus(request.getIsVerified()));

        Page<User> userPage = userRepository.findAll(spec, pageable);

        // Map to DTO Response
        return userPage.map(userMapper::toResponse);
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "User not found"));
        return userMapper.toResponse(user);
    }

    // --- Get User Public (Limited Info) ---
    public UserResponse getUserPublicById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "User not found"));
        return userMapper.toPublicResponse(user); // เรียกใช้ method ใหม่ใน Mapper
    }

    // --- Update User (General & Admin) ---
    public UserResponse updateUser(String id, UserUpdateRequest request,
                                   MultipartFile nationalIdPhoto,
                                   MultipartFile selfiePhoto,
                                   MultipartFile profilePicture) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "User not found"));

        // Update Text Fields (เช็ค null/empty ก่อน update)
        if (StringUtils.hasText(request.getFirstName())) user.setFirstName(request.getFirstName());
        if (StringUtils.hasText(request.getLastName())) user.setLastName(request.getLastName());
        if (StringUtils.hasText(request.getPhoneNumber())) user.setPhoneNumber(request.getPhoneNumber());
        if (StringUtils.hasText(request.getGender())) user.setGender(request.getGender());

        // Handle File Uploads
        if (nationalIdPhoto != null && !nationalIdPhoto.isEmpty()) {
            // String url = cloudinaryService.upload(nationalIdPhoto, "national_ids");
            user.setNationalIdPhotoUrl("mock_updated_national_url");
        }
        if (selfiePhoto != null && !selfiePhoto.isEmpty()) {
            // String url = cloudinaryService.upload(selfiePhoto, "selfies");
            user.setSelfiePhotoUrl("mock_updated_selfie_url");
        }
        if (profilePicture != null && !profilePicture.isEmpty()) {
            // String url = cloudinaryService.upload(profilePicture, "profiles");
            user.setProfilePicture("mock_updated_profile_url");
        }

        // Admin Fields (ถ้าส่งมาเป็น null คือไม่แก้)
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getIsActive() != null) user.setIsActive(request.getIsActive());
        if (request.getIsVerified() != null) user.setIsVerified(request.getIsVerified());

        return userMapper.toResponse(userRepository.save(user));
    }

    // --- Update Status Only ---
    public UserResponse updateUserStatus(String id, UserStatusUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "User not found"));

        if (request.getIsActive() != null) user.setIsActive(request.getIsActive());
        if (request.getIsVerified() != null) user.setIsVerified(request.getIsVerified());

        // TODO: Notification Logic if verified status changed

        return userMapper.toResponse(userRepository.save(user));
    }

    // --- Delete User ---
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ApiError(404, "User not found");
        }
        userRepository.deleteById(id);
    }

}