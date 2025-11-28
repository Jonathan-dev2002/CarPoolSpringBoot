package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByPassengerIdOrderByCreatedAtDesc(String passengerId);
}