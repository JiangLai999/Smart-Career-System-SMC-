package com.example.smartcareer.aspect;

import com.example.smartcareer.entity.SystemLog;
import com.example.smartcareer.repository.SystemLogRepository;
import com.example.smartcareer.util.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class SystemLogAspect {
    
    @Autowired
    private SystemLogRepository systemLogRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Pointcut("execution(* com.example.smartcareer.controller..*.*(..))")
    public void controllerPointcut() {}
    
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        String requestUri = request.getRequestURI();
        
        if (requestUri.contains("/login") || requestUri.contains("/register") || 
            requestUri.contains("/swagger") || requestUri.contains("/api-docs") ||
            requestUri.contains("/static") || requestUri.contains("/health")) {
            return joinPoint.proceed();
        }
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        String operationType = getOperationType(requestUri, method.getName());
        if (operationType == null) {
            return joinPoint.proceed();
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
        
        SystemLog systemLog = new SystemLog();
        systemLog.setUserId(userId);
        systemLog.setUsername(username);
        systemLog.setUserType(userType);
        systemLog.setOperationType(operationType);
        systemLog.setOperationDetail(getOperationDetail(requestUri, method.getName()));
        systemLog.setIpAddress(getClientIp(request));
        systemLog.setRequestUrl(requestUri);
        systemLog.setRequestMethod(request.getMethod());
        
        Object result = null;
        int responseStatus = 200;
        String errorMessage = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            responseStatus = 500;
            errorMessage = e.getMessage();
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            systemLog.setResponseStatus(responseStatus);
            systemLog.setExecutionTime(endTime - startTime);
            systemLog.setOperationTime(LocalDateTime.now());
            
            if (errorMessage != null && errorMessage.length() > 200) {
                errorMessage = errorMessage.substring(0, 200);
            }
            systemLog.setErrorMessage(errorMessage);
            
            try {
                systemLogRepository.save(systemLog);
            } catch (Exception e) {
                System.err.println("保存日志失败: " + e.getMessage());
            }
        }
    }
    
    private String getOperationType(String uri, String methodName) {
        if (uri.contains("/admin/users") && methodName.contains("create")) return "USER_CREATE";
        if (uri.contains("/admin/users") && methodName.contains("update")) return "USER_UPDATE";
        if (uri.contains("/admin/users") && methodName.contains("delete")) return "USER_DELETE";
        if (uri.contains("/admin/users") && methodName.contains("disable")) return "USER_DISABLE";
        if (uri.contains("/admin/users") && methodName.contains("enable")) return "USER_ENABLE";
        if (uri.contains("/admin/users") && methodName.contains("warn")) return "USER_WARN";
        
        if (uri.contains("/announcement") && methodName.contains("create")) return "ANNOUNCEMENT_CREATE";
        if (uri.contains("/announcement") && methodName.contains("publish")) return "ANNOUNCEMENT_PUBLISH";
        if (uri.contains("/announcement") && methodName.contains("delete")) return "ANNOUNCEMENT_DELETE";
        if (uri.contains("/announcement") && methodName.contains("update")) return "ANNOUNCEMENT_UPDATE";
        
        if (uri.contains("/audits") && methodName.contains("approve")) return "AUDIT_APPROVE";
        if (uri.contains("/audits") && methodName.contains("reject")) return "AUDIT_REJECT";
        
        if (uri.contains("/settings") && methodName.contains("update")) return "SETTING_CHANGE";
        
        if (uri.contains("/login") && methodName.equals("login")) return "LOGIN";
        if (uri.contains("/logout")) return "LOGOUT";
        
        if (uri.contains("/job") && methodName.contains("create")) return "JOB_POST";
        if (uri.contains("/job") && methodName.contains("publish")) return "JOB_POST";
        if (uri.contains("/application") && methodName.contains("apply")) return "JOB_APPLY";
        
        return null;
    }
    
    private String getOperationDetail(String uri, String methodName) {
        return String.format("%s - %s", methodName, uri);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
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