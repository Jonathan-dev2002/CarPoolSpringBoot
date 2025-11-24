package com.miniProject.Carpool.spec;


import com.miniProject.Carpool.model.Role;
import com.miniProject.Carpool.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecification {

    //ค้นหา keyword
    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) { //ถ้ามว่างเปล่า หรือเป็น null หรือมีแต่ช่องว่าง
                return null; // ถ้าไม่มี keyword ส่ง null (ไม่กรอง)
            }
            String searchKey = "%" + keyword.toLowerCase() + "%"; // ใน sql % คือ wildcard อะไรก็ได้
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchKey)
            );
        };
    }

    //    กรอง role
    public static Specification<User> hasRole(String roleStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(roleStr)) {
                return null; // ถ้าไม่ส่ง role มา ก็ไม่ต้องกรอง
            }
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());  // แปลง String เป็น Enum
                return criteriaBuilder.equal(root.get("role"), role);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    //กรองStatus
    public static Specification<User> hasStatus(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return null; // ถ้าไม่ส่ง status มา ก็ไม่ต้องกรอง
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }
}

