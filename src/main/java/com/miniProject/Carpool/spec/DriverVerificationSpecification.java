package com.miniProject.Carpool.spec;

import com.miniProject.Carpool.model.DriverVerification;
import com.miniProject.Carpool.model.LicenseType;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.model.VerificationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DriverVerificationSpecification {

    //keyword search
    public static Specification<DriverVerification> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("licenseNumber")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstNameOnLicense")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastNameOnLicense")), searchKey)
            );
        };
    }

    //verificationStatus
    public static Specification<DriverVerification> hasStatus(String statusStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(statusStr)) {
                return null;
            }
            try {
                VerificationStatus verificationStatus = VerificationStatus.valueOf(statusStr.toUpperCase());
                return criteriaBuilder.equal(root.get("verificationStatus"), verificationStatus);
            } catch (Exception e) {
                return null;
            }
        };
    }

    //tpyeOnLicense
    public static Specification<DriverVerification> hasTypeOnLicense(String typeStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(typeStr)) {
                return null;
            }
            try {
                LicenseType licenseType = LicenseType.valueOf(typeStr.toUpperCase());
                return criteriaBuilder.equal(root.get("typeOnLicense"), licenseType);
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<DriverVerification> createdBetween(String fromDateStr, String toDateStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(fromDateStr) && !StringUtils.hasText(toDateStr)) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            try {
                // "ตั้งแต่วันที่" (From)
                if (StringUtils.hasText(fromDateStr)) {
                    // ใช้ DateTimeFormatter.ISO_DATE_TIME รับค่ารูปแบบ '2023-10-25T10:30:00.000Z'
                    LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, DateTimeFormatter.ISO_DATE_TIME);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
                }

                // "ถึงวันที่" (To)
                if (StringUtils.hasText(toDateStr)) {
                    LocalDateTime toDate = LocalDateTime.parse(toDateStr, DateTimeFormatter.ISO_DATE_TIME);
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
                }
            } catch (Exception e) {
                // ถ้า Format วันที่ผิด ให้ข้ามการกรองไป หรือจะ Log error ก็ได้
                return null;
            }

            // รวมเงื่อนไขทั้งหมดด้วย AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
