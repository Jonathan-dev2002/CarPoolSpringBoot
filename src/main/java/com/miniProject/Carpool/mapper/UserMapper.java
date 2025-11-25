package com.miniProject.Carpool.mapper;

import com.miniProject.Carpool.dto.UserResponse;
import com.miniProject.Carpool.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUsername(user.getUsername());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setRole(user.getRole());
        res.setIsActive(user.getIsActive());
        res.setIsVerified(user.getIsVerified());
        res.setCreatedAt(user.getCreatedAt());
        res.setProfilePicture(user.getProfilePicture());
        return res;
    }

    public UserResponse toPublicResponse(User user) {
        if (user == null) return null;

        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setRole(user.getRole());
        res.setIsVerified(user.getIsVerified());
        res.setCreatedAt(user.getCreatedAt());
        res.setProfilePicture(user.getProfilePicture());
        return res;
    }
}