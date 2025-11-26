package com.miniProject.Carpool.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniProject.Carpool.dto.Location;
import com.miniProject.Carpool.dto.RouteResponse;
import com.miniProject.Carpool.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteMapper {

    private final ObjectMapper objectMapper; // Spring มีให้อยู่แล้ว

    public RouteResponse toResponse(Route route) {
        if (route == null) return null;

        RouteResponse res = new RouteResponse();
        res.setId(route.getId());
        res.setDriverId(route.getDriverId());
        res.setVehicleId(route.getVehicleId());
        res.setDepartureTime(route.getDepartureTime());
        res.setAvailableSeats(route.getAvailableSeats());
        res.setPricePerSeat(route.getPricePerSeat());
        res.setConditions(route.getConditions());
        res.setStatus(route.getStatus());

        // Google Maps Info
        res.setRouteSummary(route.getRouteSummary());
        res.setDistance(route.getDistance());
        res.setDuration(route.getDuration());
        res.setRoutePolyline(route.getRoutePolyline());

        // แปลง JSON String กลับเป็น Location Object
        res.setStartLocation(parseJson(route.getStartLocation(), Location.class));
        res.setEndLocation(parseJson(route.getEndLocation(), Location.class));

        return res;
    }

    // Helper แปลง String -> Object
    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            return json == null ? null : objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // Helper แปลง Object -> String (ใช้ตอน Create Route)
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}