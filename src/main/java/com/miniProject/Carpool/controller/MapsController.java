package com.miniProject.Carpool.controller;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.miniProject.Carpool.dto.ApiResponse;
import com.miniProject.Carpool.dto.Location;
import com.miniProject.Carpool.dto.RouteRequest; // Reuse for params
import com.miniProject.Carpool.service.GoogleMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class MapsController {

    private final GoogleMapsService googleMapsService;

    @GetMapping("/geocode")
    public ResponseEntity<?> geocode(@RequestParam String address) {
        GeocodingResult[] results = googleMapsService.geocode(address);
        return ResponseEntity.ok(ApiResponse.success(200, "Geocoded", results));
    }

    @GetMapping("/reverse-geocode")
    public ResponseEntity<?> reverseGeocode(@RequestParam double lat, @RequestParam double lng) {
        GeocodingResult[] results = googleMapsService.reverseGeocode(lat, lng);
        return ResponseEntity.ok(ApiResponse.success(200, "Reverse Geocoded", results));
    }

    @PostMapping("/directions")
    public ResponseEntity<?> directions(@RequestBody RouteRequest req) {
        DirectionsResult res = googleMapsService.getDirections(
                req.getStartLocation(),
                req.getEndLocation(),
                req.getWaypoints(),
                Boolean.TRUE.equals(req.getOptimizeWaypoints()),
                req.getDepartureTime()
        );
        return ResponseEntity.ok(ApiResponse.success(200, "Directions retrieved", res));
    }
}