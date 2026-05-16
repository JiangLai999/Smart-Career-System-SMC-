package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.LoginRequest;
import com.example.smartcareer.dto.response.*;
import com.example.smartcareer.entity.User;
import com.example.smartcareer.repository.UserRepository;
import com.example.smartcareer.service.AdminService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        User admin = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        
        if (admin == null) {
            return ApiResponse.error("管理员账号不存在");
        }
        
        if (!"SYSTEM_ADMIN".equals(admin.getUserType())) {
            return ApiResponse.error("非管理员账号");
        }
        
        if (admin.getStatus() == null || admin.getStatus() != 1) {
            return ApiResponse.error("账号已被禁用");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ApiResponse.error("密码错误");
        }
        
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), admin.getUserType());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        data.put("userId", admin.getId());
        data.put("username", admin.getUsername());
        data.put("userType", admin.getUserType());
        
        return ApiResponse.success(data);
    }
    
    @GetMapping("/users")
    public ApiResponse<Page<UserInfoResponse>> getAllUsers(
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserInfoResponse> users = adminService.getAllUsers(userType, keyword, page, size);
        return ApiResponse.success(users);
    }
    
    @GetMapping("/users/{userId}")
    public ApiResponse<Map<String, Object>> getUserDetail(@PathVariable Long userId) {
        Map<String, Object> user = adminService.getUserDetail(userId);
        return ApiResponse.success(user);
    }
    
    @PostMapping("/users/{userId}/disable")
    public ApiResponse<String> disableUser(@PathVariable Long userId) {
        adminService.disableUser(userId);
        return ApiResponse.success("用户已禁用");
    }
    
    @PostMapping("/users/{userId}/enable")
    public ApiResponse<String> enableUser(@PathVariable Long userId) {
        adminService.enableUser(userId);
        return ApiResponse.success("用户已启用");
    }
    
    @DeleteMapping("/users/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ApiResponse.success("用户已删除");
    }
    
    @PostMapping("/users/{userId}/warn")
    public ApiResponse<String> warnUser(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        adminService.warnUser(userId, reason);
        return ApiResponse.success("已发送警告");
    }
    
    @PutMapping("/users/{userId}")
    public ApiResponse<String> updateUser(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        adminService.updateUser(userId, request);
        return ApiResponse.success("用户信息已更新");
    }
    
    @GetMapping("/audits/pending")
    public ApiResponse<Page<EnterpriseAuditResponse>> getPendingAudits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EnterpriseAuditResponse> audits = adminService.getPendingAudits(page, size);
        return ApiResponse.success(audits);
    }
    
    @GetMapping("/audits")
    public ApiResponse<Page<EnterpriseAuditResponse>> getAllAudits(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EnterpriseAuditResponse> audits = adminService.getAllAudits(status, page, size);
        return ApiResponse.success(audits);
    }
    
    @GetMapping("/audits/{auditId}")
    public ApiResponse<Map<String, Object>> getAuditDetail(@PathVariable Long auditId) {
        Map<String, Object> audit = adminService.getAuditDetail(auditId);
        return ApiResponse.success(audit);
    }
    
    @PostMapping("/audits/{auditId}/approve")
    public ApiResponse<String> approveEnterprise(
            @PathVariable Long auditId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long adminId = getCurrentAdminId(httpRequest);
        String comment = request.get("comment");
        adminService.approveEnterprise(auditId, adminId, comment);
        return ApiResponse.success("企业审核已通过");
    }
    
    @PostMapping("/audits/{auditId}/reject")
    public ApiResponse<String> rejectEnterprise(
            @PathVariable Long auditId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long adminId = getCurrentAdminId(httpRequest);
        String comment = request.get("comment");
        adminService.rejectEnterprise(auditId, adminId, comment);
        return ApiResponse.success("企业审核已拒绝");
    }
    
    @PostMapping("/audits/{auditId}/request-info")
    public ApiResponse<String> requestInfo(
            @PathVariable Long auditId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long adminId = getCurrentAdminId(httpRequest);
        String content = request.get("content");
        adminService.requestAuditInfo(auditId, adminId, content);
        return ApiResponse.success("已通知企业补充信息");
    }
    
    @GetMapping("/logs")
    public ApiResponse<Page<SystemLogResponse>> getSystemLogs(
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<SystemLogResponse> logs = adminService.getSystemLogs(operationType, username, startTime, endTime, page, size);
        return ApiResponse.success(logs);
    }
    
    @GetMapping("/settings")
    public ApiResponse<List<SystemSettingResponse>> getAllSettings() {
        List<SystemSettingResponse> settings = adminService.getAllSettings();
        return ApiResponse.success(settings);
    }
    
    @GetMapping("/settings/category/{category}")
    public ApiResponse<Page<SystemSettingResponse>> getSettingsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SystemSettingResponse> settings = adminService.getSettingsByCategory(category, page, size);
        return ApiResponse.success(settings);
    }
    
    @PutMapping("/settings/{settingId}")
    public ApiResponse<String> updateSetting(
            @PathVariable Long settingId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long adminId = getCurrentAdminId(httpRequest);
        String value = request.get("value");
        String reason = request.get("reason");
        adminService.updateSetting(settingId, value, adminId, reason);
        return ApiResponse.success("设置已更新");
    }
    
    @GetMapping("/settings/logs")
    public ApiResponse<Page<SettingChangeLogResponse>> getSettingChangeLogs(
            @RequestParam(required = false) Long settingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SettingChangeLogResponse> logs = adminService.getSettingChangeLogs(settingId, page, size);
        return ApiResponse.success(logs);
    }
    
    @GetMapping("/settings/logs/all")
    public ApiResponse<Page<SettingChangeLogResponse>> getAllChangeLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SettingChangeLogResponse> logs = adminService.getAllChangeLogs(page, size);
        return ApiResponse.success(logs);
    }
    
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ApiResponse.success(stats);
    }
    
    private Long getCurrentAdminId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
}