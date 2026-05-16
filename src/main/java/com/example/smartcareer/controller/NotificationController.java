package com.example.smartcareer.controller;

import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.entity.Notification;
import com.example.smartcareer.service.NotificationService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/list")
    public ApiResponse<List<Notification>> getUserNotifications(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ApiResponse.success(notifications);
    }
    
    @GetMapping("/page")
    public ApiResponse<Page<Notification>> getUserNotificationsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        Page<Notification> notifications = notificationService.getUserNotifications(userId, page, size);
        return ApiResponse.success(notifications);
    }
    
    @GetMapping("/unread")
    public ApiResponse<List<Notification>> getUnreadNotifications(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ApiResponse.success(notifications);
    }
    
    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        Long count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }
    
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getNotificationStatistics(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        Map<String, Object> statistics = notificationService.getNotificationStatistics(userId);
        return ApiResponse.success(statistics);
    }
    
    @GetMapping("/{notificationId}")
    public ApiResponse<Notification> getNotificationById(@PathVariable Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        return ApiResponse.success(notification);
    }
    
    @PutMapping("/{notificationId}/read")
    public ApiResponse<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.success("已标记为已读");
    }
    
    @PutMapping("/read-all")
    public ApiResponse<String> markAllAsRead(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        notificationService.markAllAsRead(userId);
        return ApiResponse.success("全部标记为已读");
    }
    
    @DeleteMapping("/{notificationId}")
    public ApiResponse<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.success("通知已删除");
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
}
