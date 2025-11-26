package com.miniProject.Carpool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    private String firstNameOnLicense;
    private String lastNameOnLicense;

    private LocalDateTime licenseIssueDate;
    private LocalDateTime licenseExpiryDate;

    private String licensePhotoUrl;
    private String selfiePhotoUrl;

    @Enumerated(EnumType.STRING)
    private LicenseType typeOnLicense;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
