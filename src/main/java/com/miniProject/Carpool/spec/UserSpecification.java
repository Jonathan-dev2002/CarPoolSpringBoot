package com.miniProject.Carpool.spec;


import com.miniProject.Carpool.model.Role;
import com.miniProject.Carpool.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    // ค้นหา Keyword (Text Search)
    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null; // ไม่กรอง
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchKey), criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchKey), criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchKey), criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchKey), criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), searchKey));
        };
    }

    // กรอง Role
    public static Specification<User> hasRole(String roleStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(roleStr)) {
                return null;
            }
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());
                return criteriaBuilder.equal(root.get("role"), role);
            } catch (Exception e) {
                return null;
            }
        };
    }

    // กรอง Active Status
    public static Specification<User> hasActiveStatus(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    //กรอง Verified Status
    public static Specification<User> hasVerifiedStatus(Boolean isVerified) {
        return (root, query, criteriaBuilder) -> {
            if (isVerified == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("isVerified"), isVerified);
        };
    }

    // กรอง Date Range (Created At) - แถมให้เผื่อใช้ครับ
//    public static Specification<User> createdBetween(String fromDateStr, String toDateStr) {
//        return (root, query, criteriaBuilder) -> {
//            if (!StringUtils.hasText(fromDateStr) && !StringUtils.hasText(toDateStr)) {
//                return null;
//            }
//
//            // Logic คร่าวๆ: แปลง String เป็น LocalDateTime แล้วใช้ criteriaBuilder.between หรือ greaterThan/lessThan
//            // (ละไว้ก่อนเพื่อความกระชับ ถ้าจะใช้บอกได้ครับ)
//            return null;
//        };
//    }
    public static Specification<User> createdBetween(String fromDateStr, String toDateStr) {
        return (root, query, criteriaBuilder) -> {
            // ถ้าไม่มีค่าส่งมาเลยทั้งคู่ ให้ return null (ไม่กรอง)
            if (!StringUtils.hasText(fromDateStr) && !StringUtils.hasText(toDateStr)) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            try {
                // 1. กรณีมี "ตั้งแต่วันที่" (From)
                if (StringUtils.hasText(fromDateStr)) {
                    // ใช้ DateTimeFormatter.ISO_DATE_TIME รับค่ารูปแบบ '2023-10-25T10:30:00.000Z'
                    LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, DateTimeFormatter.ISO_DATE_TIME);
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
                }

                // 2. กรณีมี "ถึงวันที่" (To)
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

