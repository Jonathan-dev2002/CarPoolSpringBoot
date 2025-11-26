package com.miniProject.Carpool.service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import com.miniProject.Carpool.dto.Location;
import com.miniProject.Carpool.util.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMapsService {

    private final GeoApiContext geoApiContext;

    // แปลง Location Object ให้เป็น String ที่ Google เข้าใจ
    private String normalizeLocation(Object loc) {
        if (loc == null) return null;

        if (loc instanceof Location l) {
            if (StringUtils.hasText(l.getPlaceId())) return "place_id:" + l.getPlaceId();
            if (l.getLat() != null && l.getLng() != null) return l.getLat() + "," + l.getLng();
            if (StringUtils.hasText(l.getAddress())) return l.getAddress();
        }
        return null;
    }

    public DirectionsResult getDirections(Location origin, Location destination, List<String> waypoints, boolean optimize, LocalDateTime departureTime) {
        try {
            DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                    .origin(normalizeLocation(origin))
                    .destination(normalizeLocation(destination))
                    .mode(TravelMode.DRIVING)
                    .alternatives(false)
                    .language("th")
                    .region("th");

            if (waypoints != null && !waypoints.isEmpty()) {
                request.waypoints(waypoints.toArray(new String[0]));
                request.optimizeWaypoints(optimize);
            }

            if (departureTime != null) {
                request.departureTime(departureTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                request.departureTime(Instant.now());
            }

            return request.await(); // เรียก API จริง
        } catch (Exception e) {
            log.error("Google Maps API Error", e);
            throw new ApiError(500, "Google Maps API Error: " + e.getMessage());
        }
    }

    // เผื่อใช้ Geocode / Reverse Geocode
    public GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(geoApiContext, address).language("th").await();
        } catch (Exception e) {
            throw new ApiError(500, "Geocoding Error");
        }
    }

    public GeocodingResult[] reverseGeocode(double lat, double lng) {
        try {
            return GeocodingApi.reverseGeocode(geoApiContext, new LatLng(lat, lng)).language("th").await();
        } catch (Exception e) {
            throw new ApiError(500, "Reverse Geocoding Error");
        }
    }
}