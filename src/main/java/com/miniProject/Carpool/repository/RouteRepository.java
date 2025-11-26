package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.Route;
import com.miniProject.Carpool.model.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    List<Route> findAllByOrderByCreatedAtDesc();
    List<Route> findByDriverIdAndStatus(String driverId, RouteStatus status);
}