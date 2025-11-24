package com.miniProject.Carpool.service;


import com.miniProject.Carpool.dto.UpdateStatusRequest;
import com.miniProject.Carpool.dto.UserDto;
import com.miniProject.Carpool.dto.UserSearchRequest;
import com.miniProject.Carpool.model.Role;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.miniProject.Carpool.spec.UserSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
    public Page<User> getAllUsers(UserSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        // เริ่มต้นด้วย where (เงื่อนไขแรก) แล้วต่อด้วย and (เงื่อนไขต่อๆไป)
        Specification<User> spec = Specification.where(UserSpecification.hasKeyword(request.getKeyword()))
                .and(UserSpecification.hasRole(request.getRole()))
                .and(UserSpecification.hasStatus(request.getIsActive()));

        return userRepository.findAll(spec, pageable);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(UserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase())); //String -> enum obj + upper case
            } catch (IllegalArgumentException e) {
                user.setRole(Role.USER);
            }
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserDto request) {
        User user = getUserById(id);
        if (user == null) throw new RuntimeException("User not found");

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public User updateCurrentUser(String email, UserDto request) {
        User user = getUserByEmail(email);
        if (user == null) throw new RuntimeException("User not found");

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public User changeRole(UUID id, Role newRole) {
        User user = getUserById(id);
        if (user == null) throw new RuntimeException("User not found");

        user.setRole(newRole);
        return userRepository.save(user);
    }

    public User updateUserStatus(UUID id, UpdateStatusRequest request) {
        User user = getUserById(id);
        if (user == null) throw new RuntimeException("User not found");
        user.setIsActive(request.getIsActive());
        return userRepository.save(user);
    }
}