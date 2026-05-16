package com.example.smartcareer.config;

import com.example.smartcareer.entity.SystemLog;
import com.example.smartcareer.repository.SystemLogRepository;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
public class SystemLogInterceptor implements HandlerInterceptor {
    
    @Autowired
    private SystemLogRepository systemLogRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uri = request.getRequestURI();
        
        if (uri.contains("/login") || uri.contains("/register") || 
            uri.contains("/swagger") || uri.contains("/api-docs") ||
            uri.contains("/static") || uri.contains("/health") ||
            uri.contains("/announcement/active") || uri.contains("/notification")) {
            return;
        }
        
        if (!uri.contains("/admin/") && !uri.contains("/company/")) {
            return;
        }
        
        String operationType = getOperationType(uri);
        if (operationType == null) {
            return;
        }
        
        String username = "System";
        Long userId = null;
        String userType = null;
        
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring(7);
                username = jwtUtil.extractUsername(token);
                userId = jwtUtil.extractUserId(token);
                userType = jwtUtil.extractUserType(token);
            } catch (Exception e) {
                // ignore
            }
        }
        
        SystemLog log = new SystemLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setUserType(userType);
        log.setOperationType(operationType);
        log.setOperationDetail(request.getMethod() + " " + uri);
        log.setIpAddress(getClientIp(request));
        log.setRequestUrl(uri);
        log.setRequestMethod(request.getMethod());
        log.setResponseStatus(response.getStatus());
        log.setOperationTime(LocalDateTime.now());
        
        try {
            systemLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("保存日志失败: " + e.getMessage());
        }
    }
    
    private String getOperationType(String uri) {
        if (uri.contains("/admin/users")) {
            if (uri.contains("disable")) return "USER_DISABLE";
            if (uri.contains("enable")) return "USER_ENABLE";
            if (uri.contains("warn")) return "USER_WARN";
            if (uri.contains("delete")) return "USER_DELETE";
            return "USER_VIEW";
        }
        if (uri.contains("/admin/announcements")) {
            if (uri.contains("publish")) return "ANNOUNCEMENT_PUBLISH";
            if (uri.contains("delete")) return "ANNOUNCEMENT_DELETE";
            return "ANNOUNCEMENT_VIEW";
        }
        if (uri.contains("/admin/audits")) {
            if (uri.contains("approve")) return "AUDIT_APPROVE";
            if (uri.contains("reject")) return "AUDIT_REJECT";
            return "AUDIT_VIEW";
        }
        if (uri.contains("/admin/settings")) return "SETTING_VIEW";
        if (uri.contains("/admin/logs")) return "LOGS_VIEW";
        if (uri.contains("/admin/dashboard")) return "DASHBOARD_VIEW";
        
        if (uri.contains("/company/")) return "COMPANY_OPERATION";
        
        return "OPERATION";
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}