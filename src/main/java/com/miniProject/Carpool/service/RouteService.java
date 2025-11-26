package com.miniProject.Carpool.service;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.miniProject.Carpool.dto.RouteRequest;
import com.miniProject.Carpool.dto.RouteResponse;
import com.miniProject.Carpool.mapper.RouteMapper;
import com.miniProject.Carpool.model.Route;
import com.miniProject.Carpool.model.RouteStatus;
import com.miniProject.Carpool.repository.RouteRepository;
import com.miniProject.Carpool.util.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final GoogleMapsService googleMapsService;
    private final RouteMapper routeMapper;

    // --- Get All Routes ---
    public List<RouteResponse> getAllRoutes() {
        List<Route> routes = routeRepository.findAllByOrderByCreatedAtDesc();
        return routes.stream()
                .map(routeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RouteResponse getRouteById(String id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "Route not found"));
        return routeMapper.toResponse(route);
    }

    // --- Create Route ---
    @Transactional
    public RouteResponse createRoute(String driverId, RouteRequest request) {
        Route route = new Route();

        //เรียก Google Maps เพื่อคำนวณเส้นทาง
        enrichRouteWithGoogleData(route, request);

        //Set ข้อมูลทั่วไป
        route.setDriverId(driverId);
        route.setVehicleId(request.getVehicleId());
        route.setDepartureTime(request.getDepartureTime());
        route.setAvailableSeats(request.getAvailableSeats());
        route.setPricePerSeat(request.getPricePerSeat());
        route.setConditions(request.getConditions());
        route.setStatus(RouteStatus.AVAILABLE);

        // 3. แปลง Location Object เป็น JSON String เพื่อเก็บลง DB
        route.setStartLocation(routeMapper.toJson(request.getStartLocation()));
        route.setEndLocation(routeMapper.toJson(request.getEndLocation()));

        Route savedRoute = routeRepository.save(route);
        return routeMapper.toResponse(savedRoute);
    }

    private void enrichRouteWithGoogleData(Route route, RouteRequest request) {
        // เรียก Service ที่เราเขียนไว้
        DirectionsResult directions = googleMapsService.getDirections(
                request.getStartLocation(),
                request.getEndLocation(),
                request.getWaypoints(),
                Boolean.TRUE.equals(request.getOptimizeWaypoints()),
                request.getDepartureTime()
        );

        if (directions.routes != null && directions.routes.length > 0) {
            DirectionsRoute primary = directions.routes[0];

            // คำนวณผลรวมระยะทางและเวลาจากทุก Leg
            long sumMeters = Arrays.stream(primary.legs).mapToLong(l -> l.distance.inMeters).sum();
            long sumSeconds = Arrays.stream(primary.legs).mapToLong(l -> l.duration.inSeconds).sum();

            // Set ค่าที่ได้จาก Google
            route.setRoutePolyline(primary.overviewPolyline.getEncodedPath());
            route.setRouteSummary(primary.summary);
            route.setDistanceMeters((int) sumMeters);
            route.setDurationSeconds((int) sumSeconds);

            // สร้าง String ให้อ่านง่าย เช่น "10 km"
            String distText = Arrays.stream(primary.legs)
                    .map(l -> l.distance.humanReadable)
                    .collect(Collectors.joining(" + "));
            route.setDistance(distText);

            String durText = Arrays.stream(primary.legs)
                    .map(l -> l.duration.humanReadable)
                    .collect(Collectors.joining(" + "));
            route.setDuration(durText);
        }
    }
}