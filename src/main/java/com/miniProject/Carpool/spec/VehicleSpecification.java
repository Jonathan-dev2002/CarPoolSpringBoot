package com.miniProject.Carpool.spec;

import com.miniProject.Carpool.model.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VehicleSpecification {

    //keyword search
    public static Specification<Vehicle> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("licensePlate")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicleModel")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicleType")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("color")), searchKey)
            );
        };
    }

    //license plate search
    public static Specification<Vehicle> hasLicensePlate(String licensePlateStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(licensePlateStr)) {
                return null;
            }
            String searchKey = "%" + licensePlateStr.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("licensePlate")), searchKey);
        };
    }

    //vehicle type search
    public static Specification<Vehicle> hasVehicleType(String vehicleTypeStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(vehicleTypeStr)) {
                return null;
            }
            String searchKey = "%" + vehicleTypeStr.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicleType")), searchKey);
        };
    }

    //seat capacity search
    public static Specification<Vehicle> hasSeatCapacity(Integer seatCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (seatCapacity == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("seatCapacity"), seatCapacity);
            //return criteriaBuilder.greaterThanOrEqualTo(root.get("seatCapacity"), seatCapacity);
        };
    }

    //for user
    public static Specification<Vehicle> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(userId)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }
}