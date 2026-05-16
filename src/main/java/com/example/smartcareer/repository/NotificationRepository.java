package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreateTimeDesc(Long userId);
    
    Page<Notification> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadOrderByCreateTimeDesc(Long userId, Integer isRead);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.isRead = 0")
    Long countUnreadByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 1, n.readTime = CURRENT_TIMESTAMP WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 1, n.readTime = CURRENT_TIMESTAMP WHERE n.userId = :userId AND n.isRead = 0")
    void markAllAsRead(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
