package com.miniProject.Carpool.spec;


import com.miniProject.Carpool.model.Role;
import com.miniProject.Carpool.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

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
    public static Specification<User> createdBetween(String fromDateStr, String toDateStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(fromDateStr) && !StringUtils.hasText(toDateStr)) {
                return null;
            }

            // Logic คร่าวๆ: แปลง String เป็น LocalDateTime แล้วใช้ criteriaBuilder.between หรือ greaterThan/lessThan
            // (ละไว้ก่อนเพื่อความกระชับ ถ้าจะใช้บอกได้ครับ)
            return null;
        };
    }
}

