package com.example.smartcareer.service;

import com.example.smartcareer.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    
    Notification createNotification(Long userId, String type, String title, String content, Long relatedId, String relatedType);
    
    Notification sendHireNotification(Long userId, Long applicationId, String jobTitle, String companyName);
    
    Notification sendInterviewNotification(Long userId, Long interviewId, String jobTitle, String interviewTime, String location);
    
    Notification sendRejectNotification(Long userId, Long applicationId, String jobTitle, String companyName);
    
    Notification sendWarningNotification(Long userId, String reason);
    
    Notification getNotificationById(Long notificationId);
    
    List<Notification> getUserNotifications(Long userId);
    
    Page<Notification> getUserNotifications(Long userId, int page, int size);
    
    List<Notification> getUnreadNotifications(Long userId);
    
    Long getUnreadCount(Long userId);
    
    void markAsRead(Long notificationId);
    
    void markAllAsRead(Long userId);
    
    void deleteNotification(Long notificationId);
    
    Map<String, Object> getNotificationStatistics(Long userId);
}
