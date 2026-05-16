package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.response.*;
import com.example.smartcareer.entity.*;
import com.example.smartcareer.repository.*;
import com.example.smartcareer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;

@Service
public class AdminServiceImpl implements AdminService {
    
    private static final long startTime = System.currentTimeMillis();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Autowired
    private EnterpriseAuditRepository enterpriseAuditRepository;
    
    @Autowired
    private SystemLogRepository systemLogRepository;
    
    @Autowired
    private SystemSettingRepository systemSettingRepository;
    
    @Autowired
    private SettingChangeLogRepository settingChangeLogRepository;
    
    @Autowired
    private SystemAdminRepository systemAdminRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Override
    public Page<UserInfoResponse> getAllUsers(String userType, String keyword, int page, int size) {
        List<User> allUsers = userRepository.findAll();
        
        Map<Long, String> enterpriseNames = new HashMap<>();
        List<Object[]> hrData = enterpriseHRRepository.findAllEnterpriseHRWithEnterprise();
        for (Object[] row : hrData) {
            Long hrId = ((Number) row[0]).longValue();
            Object enterpriseNameObj = row[8];
            if (enterpriseNameObj != null) {
                String enterpriseName = enterpriseNameObj.toString();
                enterpriseNames.put(hrId, enterpriseName);
            }
        }
        
        List<User> filteredUsers = allUsers.stream()
            .filter(user -> {
                boolean matches = true;
                if (userType != null && !userType.isEmpty()) {
                    if (userType.equals("JOB_SEEKER")) {
                        matches = user instanceof JobSeeker;
                    } else if (userType.equals("ENTERPRISE_HR")) {
                        matches = user instanceof EnterpriseHR;
                    } else if (userType.equals("SYSTEM_ADMIN")) {
                        matches = user instanceof SystemAdmin;
                    }
                }
                if (keyword != null && !keyword.isEmpty() && matches) {
                    String lowerKeyword = keyword.toLowerCase();
                    matches = user.getUsername().toLowerCase().contains(lowerKeyword) ||
                               (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerKeyword));
                }
                return matches;
            })
            .collect(Collectors.toList());
        
        int totalElements = filteredUsers.size();
        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<User> pagedUsers = filteredUsers.subList(start, end);
        
        List<UserInfoResponse> content = pagedUsers.stream()
            .map(user -> convertToUserInfo(user, enterpriseNames))
            .collect(Collectors.toList());
        
        Pageable pageable = PageRequest.of(page, size);
        return new org.springframework.data.domain.PageImpl<>(content, pageable, totalElements);
    }
    
    private UserInfoResponse convertToUserInfo(User user, Map<Long, String> enterpriseNames) {
        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus() == 1 ? "ACTIVE" : "DISABLED");
        response.setCreatedTime(user.getCreateTime());
        
        if (user instanceof JobSeeker) {
            JobSeeker js = (JobSeeker) user;
            response.setRealName(js.getRealName());
        } else if (user instanceof EnterpriseHR) {
            EnterpriseHR hr = (EnterpriseHR) user;
            response.setRealName(hr.getRealName());
            response.setEnterpriseName(enterpriseNames.get(hr.getId()));
        } else if (user instanceof SystemAdmin) {
            SystemAdmin admin = (SystemAdmin) user;
            response.setRealName(admin.getRealName());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getUserDetail(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setUserType(user.getUserType());
        userInfo.setStatus(user.getStatus() == 1 ? "ACTIVE" : "DISABLED");
        userInfo.setCreatedTime(user.getCreateTime());
        
        if (user instanceof JobSeeker) {
            JobSeeker js = (JobSeeker) user;
            userInfo.setRealName(js.getRealName());
        } else if (user instanceof EnterpriseHR) {
            EnterpriseHR hr = (EnterpriseHR) user;
            userInfo.setRealName(hr.getRealName());
            String enterpriseName = enterpriseHRRepository.findEnterpriseNameByHrId(hr.getId());
            if (enterpriseName != null && (enterpriseName.contains("?") || enterpriseName.contains("�"))) {
                try {
                    byte[] bytes = enterpriseName.getBytes("ISO-8859-1");
                    enterpriseName = new String(bytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // 如果转换失败，使用原始字符串
                }
            }
            userInfo.setEnterpriseName(enterpriseName);
        } else if (user instanceof SystemAdmin) {
            SystemAdmin admin = (SystemAdmin) user;
            userInfo.setRealName(admin.getRealName());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", userInfo);
        result.put("loginHistory", new ArrayList<>());
        
        return result;
    }
    
    @Override
    @Transactional
    public void disableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        if ("SYSTEM_ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("不能禁用管理员账号");
        }
        
        user.setStatus(0);
        userRepository.save(user);
        
        logOperation("USER_DISABLE", user.getUsername(), "禁用用户");
    }
    
    @Override
    @Transactional
    public void enableUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        if ("SYSTEM_ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("不能启用管理员账号");
        }
        
        user.setStatus(1);
        userRepository.save(user);
        
        logOperation("USER_ENABLE", user.getUsername(), "启用用户");
    }
    
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        if ("SYSTEM_ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("不能删除管理员账号");
        }
        
        String username = user.getUsername();
        userRepository.deleteById(userId);
        
        logOperation("USER_DELETE", username, "删除用户");
    }
    
    @Override
    @Transactional
    public void warnUser(Long userId, String reason) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        if ("SYSTEM_ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("不能警告管理员账号");
        }
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("WARNING");
        notification.setTitle("系统警告");
        notification.setContent(String.format("您收到一条系统警告。原因：%s。请遵守平台规则，如有疑问请联系客服。", reason));
        notification.setIsRead(0);
        notificationRepository.save(notification);
        
        logOperation("USER_WARN", user.getUsername(), "警告用户: " + reason);
    }
    
    @Override
    @Transactional
    public void updateUser(Long userId, Map<String, String> request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        if (request.containsKey("username")) {
            user.setUsername(request.get("username"));
        }
        if (request.containsKey("email")) {
            user.setEmail(request.get("email"));
        }
        if (request.containsKey("phone")) {
            user.setPhone(request.get("phone"));
        }
        if (request.containsKey("userType")) {
            String newUserType = request.get("userType");
            if ("SYSTEM_ADMIN".equals(user.getUserType())) {
                throw new RuntimeException("不能修改管理员账号类型");
            }
            if ("SYSTEM_ADMIN".equals(newUserType)) {
                throw new RuntimeException("不能将用户设置为管理员");
            }
            user.setUserType(newUserType);
        }
        
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
        
        logOperation("USER_UPDATE", user.getUsername(), "更新用户信息");
    }
    
    private void logOperation(String operationType, String username, String detail) {
        SystemLog log = new SystemLog();
        log.setOperationType(operationType);
        log.setUsername(username);
        log.setOperationDetail(detail);
        log.setOperationTime(LocalDateTime.now());
        log.setUserType("SYSTEM_ADMIN");
        log.setResponseStatus(200);
        systemLogRepository.save(log);
    }
    
    private void logAdminOperation(Long adminId, String adminUsername, String operationType, String target, String detail) {
        SystemLog log = new SystemLog();
        log.setUserId(adminId);
        log.setUsername(adminUsername);
        log.setOperationType(operationType);
        log.setOperationDetail(String.format("[%s] %s: %s", target, operationType, detail));
        log.setOperationTime(LocalDateTime.now());
        log.setUserType("SYSTEM_ADMIN");
        log.setResponseStatus(200);
        systemLogRepository.save(log);
    }
    
    @Override
    public Page<EnterpriseAuditResponse> getPendingAudits(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EnterpriseAudit> audits = enterpriseAuditRepository.findByAuditStatus("PENDING", pageable);
        return audits.map(this::convertToAuditResponse);
    }
    
    @Override
    public Page<EnterpriseAuditResponse> getAllAudits(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EnterpriseAudit> audits;
        
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            audits = enterpriseAuditRepository.findByAuditStatus(status, pageable);
        } else {
            audits = enterpriseAuditRepository.findAll(pageable);
        }
        
        return audits.map(this::convertToAuditResponse);
    }
    
    private EnterpriseAuditResponse convertToAuditResponse(EnterpriseAudit audit) {
        EnterpriseAuditResponse response = new EnterpriseAuditResponse();
        response.setId(audit.getId());
        response.setAuditStatus(audit.getAuditStatus());
        response.setAuditComment(audit.getAuditComment());
        response.setSubmitTime(audit.getSubmitTime());
        response.setAuditTime(audit.getAuditTime());
        response.setUpdateTime(audit.getUpdateTime());
        
        if (audit.getEnterprise() != null) {
            Enterprise e = audit.getEnterprise();
            response.setEnterpriseId(e.getId());
            response.setEnterpriseName(e.getEnterpriseName());
            response.setIndustry(e.getIndustry());
            response.setScale(e.getScale());
            response.setDescription(e.getDescription());
            response.setLicenseNumber(e.getLicenseNumber());
            response.setContactEmail(e.getContactEmail());
            response.setContactPhone(e.getContactPhone());
            response.setAddress(e.getAddress());
        }
        
        if (audit.getAdmin() != null) {
            response.setAuditorName(audit.getAdmin().getUsername());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getAuditDetail(Long auditId) {
        Optional<EnterpriseAudit> auditOpt = enterpriseAuditRepository.findById(auditId);
        if (!auditOpt.isPresent()) {
            throw new RuntimeException("审核记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("audit", convertToAuditResponse(auditOpt.get()));
        result.put("submittedData", auditOpt.get().getSubmittedData());
        
        return result;
    }
    
    @Override
    @Transactional
    public void approveEnterprise(Long auditId, Long adminId, String comment) {
        Optional<EnterpriseAudit> auditOpt = enterpriseAuditRepository.findById(auditId);
        if (!auditOpt.isPresent()) {
            throw new RuntimeException("审核记录不存在");
        }
        
        Optional<SystemAdmin> adminOpt = systemAdminRepository.findById(adminId);
        
        EnterpriseAudit audit = auditOpt.get();
        audit.setAuditStatus("APPROVED");
        audit.setAuditComment(comment);
        audit.setAuditTime(LocalDateTime.now());
        adminOpt.ifPresent(audit::setAdmin);
        enterpriseAuditRepository.save(audit);
        
        // 更新企业审核状态
        if (audit.getEnterprise() != null) {
            Enterprise enterprise = audit.getEnterprise();
            enterprise.setAuditStatus(1); // 1 = 审核通过
            enterpriseRepository.save(enterprise);
        }
        
        String enterpriseName = audit.getEnterprise() != null ? audit.getEnterprise().getEnterpriseName() : "未知企业";
        logOperation("AUDIT_APPROVE", adminOpt.map(SystemAdmin::getUsername).orElse("管理员"), 
            "审核通过企业: " + enterpriseName + (comment != null ? ", 备注: " + comment : ""));
    }
    
    @Override
    @Transactional
    public void rejectEnterprise(Long auditId, Long adminId, String comment) {
        Optional<EnterpriseAudit> auditOpt = enterpriseAuditRepository.findById(auditId);
        if (!auditOpt.isPresent()) {
            throw new RuntimeException("审核记录不存在");
        }
        
        Optional<SystemAdmin> adminOpt = systemAdminRepository.findById(adminId);
        
        EnterpriseAudit audit = auditOpt.get();
        audit.setAuditStatus("REJECTED");
        audit.setAuditComment(comment);
        audit.setAuditTime(LocalDateTime.now());
        adminOpt.ifPresent(audit::setAdmin);
        enterpriseAuditRepository.save(audit);
        
        // 更新企业审核状态
        if (audit.getEnterprise() != null) {
            Enterprise enterprise = audit.getEnterprise();
            enterprise.setAuditStatus(2); // 2 = 审核拒绝
            enterpriseRepository.save(enterprise);
        }
        
        String enterpriseName = audit.getEnterprise() != null ? audit.getEnterprise().getEnterpriseName() : "未知企业";
        logOperation("AUDIT_REJECT", adminOpt.map(SystemAdmin::getUsername).orElse("管理员"), 
            "审核拒绝企业: " + enterpriseName + (comment != null ? ", 原因: " + comment : ""));
    }
    
    @Override
    @Transactional
    public void requestAuditInfo(Long auditId, Long adminId, String content) {
        Optional<EnterpriseAudit> auditOpt = enterpriseAuditRepository.findById(auditId);
        if (!auditOpt.isPresent()) {
            throw new RuntimeException("审核记录不存在");
        }
        
        Optional<SystemAdmin> adminOpt = systemAdminRepository.findById(adminId);
        
        EnterpriseAudit audit = auditOpt.get();
        audit.setAuditStatus("PENDING");
        audit.setAuditComment("【补充材料通知】" + content);
        audit.setAuditTime(LocalDateTime.now());
        adminOpt.ifPresent(audit::setAdmin);
        enterpriseAuditRepository.save(audit);
        
        String enterpriseName = audit.getEnterprise() != null ? audit.getEnterprise().getEnterpriseName() : "未知企业";
        logOperation("AUDIT_REQUEST_INFO", adminOpt.map(SystemAdmin::getUsername).orElse("管理员"), 
            "要求企业补充材料: " + enterpriseName);
    }
    
    @Override
    public Page<SystemLogResponse> getSystemLogs(String operationType, String username, String startTime, String endTime, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SystemLog> logs = systemLogRepository.findAll(pageable);
        
        List<SystemLogResponse> content = logs.getContent().stream()
            .filter(log -> {
                boolean matches = true;
                if (operationType != null && !operationType.isEmpty()) {
                    matches = operationType.equals(log.getOperationType());
                }
                if (username != null && !username.isEmpty() && matches) {
                    matches = log.getUsername() != null && log.getUsername().toLowerCase().contains(username.toLowerCase());
                }
                if (startTime != null && !startTime.isEmpty() && matches) {
                    try {
                        LocalDateTime start = LocalDateTime.parse(startTime + "T00:00:00");
                        matches = log.getOperationTime() != null && !log.getOperationTime().isBefore(start);
                    } catch (Exception e) {
                        // ignore parse error
                    }
                }
                if (endTime != null && !endTime.isEmpty() && matches) {
                    try {
                        LocalDateTime end = LocalDateTime.parse(endTime + "T23:59:59");
                        matches = log.getOperationTime() != null && !log.getOperationTime().isAfter(end);
                    } catch (Exception e) {
                        // ignore parse error
                    }
                }
                return matches;
            })
            .map(this::convertToLogResponse)
            .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(content, pageable, logs.getTotalElements());
    }
    
    private SystemLogResponse convertToLogResponse(SystemLog log) {
        SystemLogResponse response = new SystemLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUserId());
        response.setUsername(log.getUsername());
        response.setUserType(log.getUserType());
        response.setOperationType(log.getOperationType());
        response.setOperationDetail(log.getOperationDetail());
        response.setIpAddress(log.getIpAddress());
        response.setOperationTime(log.getOperationTime());
        response.setRequestUrl(log.getRequestUrl());
        response.setRequestMethod(log.getRequestMethod());
        response.setResponseStatus(log.getResponseStatus());
        response.setErrorMessage(log.getErrorMessage());
        return response;
    }
    
    @Override
    public List<SystemSettingResponse> getAllSettings() {
        List<SystemSetting> settings = systemSettingRepository.findAll();
        return settings.stream().map(this::convertToSettingResponse).collect(Collectors.toList());
    }
    
    @Override
    public Page<SystemSettingResponse> getSettingsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SystemSetting> settings = systemSettingRepository.findByCategory(category, pageable);
        return settings.map(this::convertToSettingResponse);
    }
    
    private SystemSettingResponse convertToSettingResponse(SystemSetting setting) {
        SystemSettingResponse response = new SystemSettingResponse();
        response.setId(setting.getId());
        response.setSettingKey(setting.getSettingKey());
        response.setSettingValue(setting.getSettingValue());
        response.setDescription(setting.getDescription());
        response.setSettingType(setting.getSettingType());
        response.setCategory(setting.getCategory());
        response.setCreatedTime(setting.getCreatedTime());
        response.setUpdatedTime(setting.getUpdatedTime());
        return response;
    }
    
    @Override
    @Transactional
    public void updateSetting(Long settingId, String value, Long adminId, String reason) {
        Optional<SystemSetting> settingOpt = systemSettingRepository.findById(settingId);
        if (!settingOpt.isPresent()) {
            throw new RuntimeException("设置不存在");
        }
        
        Optional<SystemAdmin> adminOpt = systemAdminRepository.findById(adminId);
        
        SystemSetting setting = settingOpt.get();
        String oldValue = setting.getSettingValue();
        setting.setSettingValue(value);
        systemSettingRepository.save(setting);
        
        SettingChangeLog changeLog = new SettingChangeLog();
        changeLog.setSetting(setting);
        changeLog.setSettingKey(setting.getSettingKey());
        changeLog.setOldValue(oldValue);
        changeLog.setNewValue(value);
        adminOpt.ifPresent(changeLog::setOperator);
        changeLog.setOperatorName(adminOpt.map(SystemAdmin::getUsername).orElse("未知"));
        changeLog.setChangeReason(reason);
        settingChangeLogRepository.save(changeLog);
    }
    
    @Override
    public Page<SettingChangeLogResponse> getSettingChangeLogs(Long settingId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SettingChangeLog> logs = settingChangeLogRepository.findAll(pageable);
        
        List<SettingChangeLogResponse> content = logs.getContent().stream()
            .filter(log -> settingId == null || (log.getSetting() != null && log.getSetting().getId().equals(settingId)))
            .map(this::convertToChangeLogResponse)
            .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(content, pageable, logs.getTotalElements());
    }
    
    @Override
    public Page<SettingChangeLogResponse> getAllChangeLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SettingChangeLog> logs = settingChangeLogRepository.findAll(pageable);
        return logs.map(this::convertToChangeLogResponse);
    }
    
    private SettingChangeLogResponse convertToChangeLogResponse(SettingChangeLog log) {
        SettingChangeLogResponse response = new SettingChangeLogResponse();
        response.setId(log.getId());
        response.setSettingKey(log.getSettingKey());
        response.setOldValue(log.getOldValue());
        response.setNewValue(log.getNewValue());
        response.setOperatorName(log.getOperatorName());
        response.setChangeReason(log.getChangeReason());
        response.setChangeTime(log.getChangeTime());
        return response;
    }
    
    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        
        long jobSeekerCount = userRepository.findByUserType("JOB_SEEKER").size();
        stats.put("totalJobSeekers", jobSeekerCount);
        
        long enterpriseCount = userRepository.findByUserType("ENTERPRISE_HR").size();
        stats.put("totalEnterprises", enterpriseCount);
        
        long pendingAudits = enterpriseAuditRepository.findByAuditStatus("PENDING", PageRequest.of(0, 1)).getTotalElements();
        stats.put("pendingAudits", pendingAudits);
        
        long approvedEnterprises = userRepository.findByUserType("ENTERPRISE_HR").size();
        stats.put("approvedEnterprises", approvedEnterprises);
        
        stats.put("recentLogs", systemLogRepository.count());
        stats.put("activeUsers", userRepository.count());
        
        Map<String, Object> systemStatus = new HashMap<>();
        systemStatus.put("database", checkDatabaseConnection());
        systemStatus.put("api", "正常");
        systemStatus.put("email", "未配置");
        systemStatus.put("storage", "正常");
        stats.put("systemStatus", systemStatus);
        
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("version", "v1.0.0");
        systemInfo.put("startTime", startTime);
        systemInfo.put("databaseVersion", getDatabaseVersion());
        systemInfo.put("environment", "Production");
        systemInfo.put("lastUpdate", "2026-03-11");
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("osName", System.getProperty("os.name"));
        stats.put("systemInfo", systemInfo);
        
        return stats;
    }
    
    private String checkDatabaseConnection() {
        try {
            userRepository.count();
            return "正常";
        } catch (Exception e) {
            return "异常";
        }
    }
    
    private String getDatabaseVersion() {
        return "MySQL 8.0";
    }
}