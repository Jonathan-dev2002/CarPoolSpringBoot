package com.miniProject.Carpool.spec;

import com.miniProject.Carpool.model.Booking;
import com.miniProject.Carpool.model.BookingStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {

    public static Specification<Booking> hasStatus(BookingStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Booking> hasRouteId(String routeId) {
        return (root, query, cb) -> StringUtils.hasText(routeId) ? cb.equal(root.get("route").get("id"), routeId) : null;
    }

    public static Specification<Booking> hasPassengerId(String passengerId) {
        return (root, query, cb) -> StringUtils.hasText(passengerId) ? cb.equal(root.get("passenger").get("id"), passengerId) : null;
    }

    public static Specification<Booking> hasDriverId(String driverId) {
        return (root, query, cb) -> StringUtils.hasText(driverId) ? cb.equal(root.get("route").get("driverId"), driverId) : null;
    }

    public static Specification<Booking> createdBetween(String from, String to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            try {
                if (StringUtils.hasText(from)) predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME)));
                if (StringUtils.hasText(to)) predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(to, DateTimeFormatter.ISO_DATE_TIME)));
            } catch (Exception e) { return null; }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Keyword search
    public static Specification<Booking> hasKeyword(String q) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(q)) return null;
            String likeKey = "%" + q.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("passenger").get("firstName")), likeKey),
                    cb.like(cb.lower(root.get("passenger").get("lastName")), likeKey),
                    cb.like(cb.lower(root.get("route").get("vehicle").get("licensePlate")), likeKey)
            );
        };
    }
}