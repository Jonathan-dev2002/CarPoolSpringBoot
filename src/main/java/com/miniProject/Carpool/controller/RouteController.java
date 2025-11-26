package com.miniProject.Carpool.controller;

import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.RouteRequest;
import com.miniProject.Carpool.dto.RouteResponse;
import com.miniProject.Carpool.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // POST /api/routes
    @PostMapping
    public ResponseEntity<ApiResponse<RouteResponse>> createRoute(
            @AuthenticationPrincipal String driverId,
            @Valid @RequestBody RouteRequest request
    ) {
        RouteResponse route = routeService.createRoute(driverId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Route created successfully", route));
    }

    // GET /api/routes
    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteResponse>>> getAllRoutes() {
        List<RouteResponse> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(ApiResponse.success(200, "Routes retrieved", routes));
    }

    // GET /api/routes/:id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteResponse>> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Route retrieved", routeService.getRouteById(id)));
    }
}