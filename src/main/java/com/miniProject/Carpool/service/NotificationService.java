package com.miniProject.Carpool.service;

import com.miniProject.Carpool.dto.NotificationCreateRequest;
import com.miniProject.Carpool.dto.NotificationResponse;
import com.miniProject.Carpool.dto.search.NotificationSearchRequest;
import com.miniProject.Carpool.mapper.NotificationMapper;
import com.miniProject.Carpool.model.Notification;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.repository.NotificationRepository;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.spec.NotificationSpecification;
import com.miniProject.Carpool.util.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    public Page<NotificationResponse> listNotifications(String userId, NotificationSearchRequest request) {

        String filterUserId = userId != null ? userId : request.getUserId();

        int pageNo = Math.max(0, request.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNo, request.getLimit(), Sort.by("createdAt").descending());

        Specification<Notification> spec = Specification.where(NotificationSpecification.hasUserId(filterUserId))
                .and(NotificationSpecification.hasKeyword(request.getQ()))
                .and(NotificationSpecification.hasType(request.getType()))
                .and(NotificationSpecification.isRead(request.getRead()))
                .and(NotificationSpecification.isAdminReviewed(request.getAdminReviewed()))
                .and(NotificationSpecification.createdBetween(request.getCreatedFrom(), request.getCreatedTo()));

        return notificationRepository.findAll(spec, pageable).map(notificationMapper::toResponse);
    }

    public NotificationResponse getMyNotificationById(String id, String userId) {
        Notification n = findAndCheckOwner(id,userId);
        return notificationMapper.toResponse(n);
    }

    public NotificationResponse markRead(String id, String userId) {
        Notification n = findAndCheckOwner(id, userId);
        n.setReadAt(LocalDateTime.now());
        return notificationMapper.toResponse(notificationRepository.save(n));
    }

    public NotificationResponse markUnread(String id, String userId) {
        Notification n = findAndCheckOwner(id, userId);
        n.setReadAt(null);
        return notificationMapper.toResponse(notificationRepository.save(n));
    }

    @Transactional
    public void markAllRead(String userId) {
        notificationRepository.markAllReadByUserId(userId);
    }

    // --- Delete ---
    public void deleteMyNotification(String id, String userId) {
        Notification n = findAndCheckOwner(id, userId);
        notificationRepository.delete(n);
    }

    public Map<String, Long> countUnread(String userId) {
        long count = notificationRepository.countByUserIdAndReadAtIsNull(userId);
        return Map.of("unread", count);
    }

    // --- Admin Actions ---
    public NotificationResponse createNotificationByAdmin(NotificationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiError(404, "User not found"));

        Notification n = Notification.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .body(request.getBody())
                .link(request.getLink())
                .metadata(request.getMetadata())
                .build();

        return notificationMapper.toResponse(notificationRepository.save(n));
    }

    public NotificationResponse adminMarkRead(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "Notification not found"));

        n.setAdminReviewedAt(LocalDateTime.now());
        return notificationMapper.toResponse(notificationRepository.save(n));
    }

    public void deleteNotificationByAdmin(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new ApiError(404, "Notification not found");
        }
        notificationRepository.deleteById(id);
    }

    private Notification findAndCheckOwner(String id, String userId) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "Notification not found"));
        if (!n.getUser().getId().equals(userId)) {
            throw new ApiError(404, "Notification not found");
        }
        return n;
    }
}