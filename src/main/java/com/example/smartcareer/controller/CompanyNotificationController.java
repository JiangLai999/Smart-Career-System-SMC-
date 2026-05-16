package com.example.smartcareer.controller;

import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.service.CompanyNotificationService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/company/notification")
public class CompanyNotificationController {
    
    @Autowired
    private CompanyNotificationService companyNotificationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/page")
    public ApiResponse<Page<Map<String, Object>>> getNotificationsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long enterpriseId = extractEnterpriseId(request);
        Long userId = extractUserId(request);
        if (enterpriseId == null) {
            return ApiResponse.success(new PageImpl<>(new ArrayList<>()));
        }
        Page<Map<String, Object>> notifications = companyNotificationService.getNotifications(enterpriseId, userId, page, size);
        return ApiResponse.success(notifications);
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(HttpServletRequest request) {
        Long enterpriseId = extractEnterpriseId(request);
        Long userId = extractUserId(request);
        if (enterpriseId == null) {
            return ApiResponse.success(0L);
        }
        Long count = companyNotificationService.getUnreadCount(enterpriseId, userId);
        return ApiResponse.success(count);
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<String> markAsRead(@PathVariable Long notificationId, HttpServletRequest request) {
        Long enterpriseId = extractEnterpriseId(request);
        Long userId = extractUserId(request);
        if (enterpriseId == null) {
            return ApiResponse.success("请先登录");
        }
        companyNotificationService.markAsRead(enterpriseId, userId, notificationId);
        return ApiResponse.success("已标记为已读");
    }

    @PutMapping("/read-all")
    public ApiResponse<String> markAllAsRead(HttpServletRequest request) {
        Long enterpriseId = extractEnterpriseId(request);
        Long userId = extractUserId(request);
        if (enterpriseId == null) {
            return ApiResponse.success("请先登录");
        }
        companyNotificationService.markAllAsRead(enterpriseId, userId);
        return ApiResponse.success("全部标记为已读");
    }
    
    private Long extractEnterpriseId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Long enterpriseId = jwtUtil.extractEnterpriseId(token);
            if (enterpriseId == null) {
                Long userId = jwtUtil.extractUserId(token);
                if (userId != null) {
                    enterpriseId = userId;
                }
            }
            return enterpriseId;
        } catch (Exception e) {
            return null;
        }
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) return null;
        if (token.startsWith("Bearer ")) token = token.substring(7);
        try {
            return jwtUtil.extractUserId(token);
        } catch (Exception e) {
            return null;
        }
    }
}
