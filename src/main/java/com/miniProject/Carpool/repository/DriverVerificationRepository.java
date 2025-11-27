package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.DriverVerification;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.model.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Driver;
import java.util.Optional;

@Repository
public interface DriverVerificationRepository extends JpaRepository<DriverVerification , String> , JpaSpecificationExecutor<DriverVerification> {
    Optional<DriverVerification> findByUser(User user);
    Optional<DriverVerification> findByUserId(String userId);
    boolean existsLicenseNumber(String licenseNumber);
}
