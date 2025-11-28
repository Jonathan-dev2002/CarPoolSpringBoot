package com.miniProject.Carpool.spec;

import com.miniProject.Carpool.model.Notification;
import com.miniProject.Carpool.model.NotificationType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<Notification> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(userId)) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Notification> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) return null;
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("body")), searchKey)
            );
        };
    }

    public static Specification<Notification> hasType(String typeStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(typeStr)) return null;
            try {
                return criteriaBuilder.equal(root.get("type"), NotificationType.valueOf(typeStr.toUpperCase()));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Notification> isRead(Boolean isRead) {
        return (root, query, criteriaBuilder) -> {
            if (isRead == null) return null;
            if (isRead) {
                return criteriaBuilder.isNotNull(root.get("readAt")); // อ่านแล้ว
            } else {
                return criteriaBuilder.isNull(root.get("readAt"));    // ยังไม่อ่าน
            }
        };
    }

    public static Specification<Notification> isAdminReviewed(Boolean isReviewed) {
        return (root, query, criteriaBuilder) -> {
            if (isReviewed == null) return null;
            if (isReviewed) {
                return criteriaBuilder.isNotNull(root.get("adminReviewedAt"));
            } else {
                return criteriaBuilder.isNull(root.get("adminReviewedAt"));
            }
        };
    }

    public static Specification<Notification> createdBetween(String fromDateStr, String toDateStr) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(fromDateStr) && !StringUtils.hasText(toDateStr)) return null;
            List<Predicate> predicates = new ArrayList<>();
            try {
                if (StringUtils.hasText(fromDateStr)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(fromDateStr, DateTimeFormatter.ISO_DATE_TIME)));
                }
                if (StringUtils.hasText(toDateStr)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(toDateStr, DateTimeFormatter.ISO_DATE_TIME)));
                }
            } catch (Exception e) { return null; }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}