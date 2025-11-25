package com.miniProject.Carpool.service;

import com.miniProject.Carpool.dto.ChangePasswordRequest;
import com.miniProject.Carpool.dto.LoginRequest;
import com.miniProject.Carpool.dto.LoginResponse;
import com.miniProject.Carpool.dto.UserResponse;
import com.miniProject.Carpool.mapper.UserMapper;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.util.ApiError;
import com.miniProject.Carpool.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper; // ✅ Inject Mapper

    public LoginResponse login(LoginRequest request) {
        User user = null;

        // 1. หา User
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
        } else if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user = userRepository.findByUsername(request.getUsername()).orElse(null);
        }

        // 2. ถ้าไม่เจอ หรือ Password ผิด
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiError(401, "Invalid credentials");
        }

        // 3. เช็ค isActive
        if (!user.getIsActive()) {
            throw new ApiError(401, "Your account has been deactivated.");
        }

        // 4. สร้าง Token
        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());

        // 5. แปลง User เป็น Response
        UserResponse userResponse = userMapper.toResponse(user); // ✅ ใช้ Mapper

        return LoginResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiError(404, "User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ApiError(401, "Incorrect current password.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ApiError(400, "New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}