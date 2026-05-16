package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.*;
import com.example.smartcareer.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AdminService {
    
    Page<UserInfoResponse> getAllUsers(String userType, String keyword, int page, int size);
    
    Map<String, Object> getUserDetail(Long userId);
    
    void disableUser(Long userId);
    
    void enableUser(Long userId);
    
    void deleteUser(Long userId);
    
    void warnUser(Long userId, String reason);
    
    void updateUser(Long userId, Map<String, String> request);
    
    Page<EnterpriseAuditResponse> getPendingAudits(int page, int size);
    
    Page<EnterpriseAuditResponse> getAllAudits(String status, int page, int size);
    
    Map<String, Object> getAuditDetail(Long auditId);
    
    void approveEnterprise(Long auditId, Long adminId, String comment);
    
    void rejectEnterprise(Long auditId, Long adminId, String comment);
    
    void requestAuditInfo(Long auditId, Long adminId, String content);
    
    Page<SystemLogResponse> getSystemLogs(String operationType, String username, String startTime, String endTime, int page, int size);
    
    List<SystemSettingResponse> getAllSettings();
    
    Page<SystemSettingResponse> getSettingsByCategory(String category, int page, int size);
    
    void updateSetting(Long settingId, String value, Long adminId, String reason);
    
    Page<SettingChangeLogResponse> getSettingChangeLogs(Long settingId, int page, int size);
    
    Page<SettingChangeLogResponse> getAllChangeLogs(int page, int size);
    
    Map<String, Object> getDashboardStats();
}