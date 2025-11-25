package com.miniProject.Carpool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String profilePicture;

    @Column(unique = true)
    private String nationalIdNumber;

    @Column(unique = true)
    private String nationalIdPhotoUrl;

    private LocalDateTime nationalIdExpiryDate;
    private String selfiePhotoUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default //ล็อกค่าเริ่มต้น
    private Role role = Role.PASSENGER;

    @Builder.Default
    private Boolean isVerified = false;

    @Builder.Default
    private Boolean isActive = true;

    private String otpCode;
    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relations (Comment ไว้ก่อนรอทำ Entity อื่นๆ)
    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private DriverVerification driverVerification;
}
