package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.DriverVerification;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> , JpaSpecificationExecutor<Vehicle> {
//    Optional<Vehicle> findVehicleByIsDefault(User user,Boolean isDefault);
    Optional<Vehicle> findByUserAndIsDefaultTrue(User user);
    Optional<Vehicle> findByIdAndUserId(String id, String userId);
}