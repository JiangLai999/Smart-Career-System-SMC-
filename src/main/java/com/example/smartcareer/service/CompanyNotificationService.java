package com.example.smartcareer.service;

import org.springframework.data.domain.Page;
import java.util.Map;

public interface CompanyNotificationService {

    Page<Map<String, Object>> getNotifications(Long enterpriseId, Long userId, int page, int size);

    Long getUnreadCount(Long enterpriseId, Long userId);

    void markAsRead(Long enterpriseId, Long userId, Long notificationId);

    void markAllAsRead(Long enterpriseId, Long userId);
}
