package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, JpaSpecificationExecutor<Notification> {
    long countByUserIdAndReadAtIsNull(String userId); // Count Unread

    // Mark All Read (Batch Update)
    // @Query จะเขียนคำสั่ง SQL (หรือ JPQL)
    @Modifying
    @Query("UPDATE Notification n SET n.readAt = CURRENT_TIMESTAMP WHERE n.user.id = :userId AND n.readAt IS NULL") //CURRENT_TIMESTAMP เป็นฟังก์ชัน ที่จะไปดึงเวลาปัจจุบันจาก db มาใส่
    void markAllReadByUserId(String userId);
}