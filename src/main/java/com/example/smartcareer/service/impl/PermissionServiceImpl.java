package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.response.PermissionResponse;
import com.example.smartcareer.dto.response.RoleResponse;
import com.example.smartcareer.entity.Permission;
import com.example.smartcareer.entity.Role;
import com.example.smartcareer.entity.UserRole;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.PermissionRepository;
import com.example.smartcareer.repository.RoleRepository;
import com.example.smartcareer.repository.UserRepository;
import com.example.smartcareer.repository.UserRoleRepository;
import com.example.smartcareer.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAllActive();
        return permissions.stream().map(this::convertToPermissionResponse).collect(Collectors.toList());
    }
    
    @Override
    public List<PermissionResponse> getPermissionsByModule(String module) {
        List<Permission> permissions = permissionRepository.findActiveByModule(module);
        return permissions.stream().map(this::convertToPermissionResponse).collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllModules() {
        return permissionRepository.findAllModules();
    }
    
    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAllActive();
        return roles.stream().map(role -> {
            RoleResponse response = convertToRoleResponse(role);
            int userCount = userRoleRepository.countByRoleId(role.getId());
            response.setUserCount(userCount);
            return response;
        }).collect(Collectors.toList());
    }
    
public Map<String, Object> getRolePermissions(Long roleId) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElse(null);
        
        if (role == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("roleId", role.getId());
        result.put("roleCode", role.getRoleCode());
        result.put("roleName", role.getRoleName());
        
        List<Long> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toList());
        result.put("permissionIds", permissionIds);
        
        return result;
    }
    
@Override
    public Map<String, Object> getRolePermissionsByCode(String roleCode) {
        Role role = roleRepository.findByRoleCode(roleCode)
                .orElse(null);

        if (role == null) {
            role = new Role();
            role.setRoleCode(roleCode);
            role.setRoleName(getRoleNameByCode(roleCode));
            role.setStatus(1);
            role.setPermissions(new HashSet<>());
            roleRepository.save(role);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("roleId", role.getId());
        result.put("roleCode", role.getRoleCode());
        result.put("roleName", role.getRoleName());

        Set<Permission> permissions = role.getPermissions();
        List<Map<String, Object>> permissionList = new ArrayList<>();

        int[] roleModules = getModulesByRole(roleCode);
        
        for (int moduleIdx : roleModules) {
            Map<String, Object> module = new HashMap<>();
            module.put("id", moduleIdx);
            module.put("name", getModuleName(moduleIdx));
            module.put("icon", getModuleIcon(moduleIdx));
            module.put("enabled", false);

            List<Map<String, Object>> perms = new ArrayList<>();
            String[] defaultPerms = getDefaultPermissionsByModule(moduleIdx - 1);
            boolean[] defaultEnabled = getDefaultEnabledByModule(roleCode, moduleIdx - 1);
            for (int j = 0; j < defaultPerms.length; j++) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", moduleIdx * 10 + j + 1);
                p.put("name", defaultPerms[j]);
                p.put("enabled", defaultEnabled[j]);
                perms.add(p);
            }
            module.put("permissions", perms);
            permissionList.add(module);
        }

        result.put("permissions", permissionList);

        return result;
    }

    private int[] getModulesByRole(String roleCode) {
        switch (roleCode) {
            case "JOB_SEEKER": return new int[]{1, 2, 3, 4};
            case "ENTERPRISE_HR": return new int[]{1, 2, 4};
            case "ENTERPRISE_ADMIN": return new int[]{1, 2, 4};
            case "SYSTEM_ADMIN": return new int[]{1, 2, 3, 4, 5};
            case "CONTENT_ADMIN": return new int[]{3, 5};
            default: return new int[]{1, 2, 3, 4, 5};
        }
    }

    private String getModuleName(int moduleIdx) {
        switch (moduleIdx) {
            case 1: return "职位管理";
            case 2: return "简历管理";
            case 3: return "申请管理";
            case 4: return "企业招聘";
            case 5: return "系统管理";
            default: return "其他";
        }
    }

    private String getModuleIcon(int moduleIdx) {
        switch (moduleIdx) {
            case 1: return "work";
            case 2: return "description";
            case 3: return "send";
            case 4: return "business";
            case 5: return "settings";
            default: return "folder";
        }
    }

    private String getRoleNameByCode(String roleCode) {
        switch (roleCode) {
            case "JOB_SEEKER": return "求职者";
            case "ENTERPRISE_HR": return "企业HR";
            case "ENTERPRISE_ADMIN": return "企业管理员";
            case "SYSTEM_ADMIN": return "系统管理员";
            case "CONTENT_ADMIN": return "内容管理员";
            default: return roleCode;
        }
    }

    private String[] getDefaultPermissionsByModule(int moduleIndex) {
        switch (moduleIndex) {
            case 0: return new String[]{"浏览职位", "搜索职位", "发布职位", "编辑职位", "删除职位", "上下线职位", "查看投递", "导出数据"};
            case 1: return new String[]{"查看简历", "创建简历", "编辑简历", "删除简历", "公开简历", "筛选简历", "下载简历", "简历评分"};
            case 2: return new String[]{"投递申请", "查看状态", "撤销申请", "处理申请", "发送邀请", "安排面试", "发送录用", "发送拒绝"};
            case 3: return new String[]{"职业测评", "查看结果", "职位推荐", "职业建议", "数据分析", "导出报告"};
            case 4: return new String[]{"用户管理", "企业审核", "权限设置", "系统设置", "查看日志", "日志搜索"};
            default: return new String[]{};
        }
    }

    private boolean[] getDefaultEnabledByModule(String roleCode, int moduleIndex) {
        if ("SYSTEM_ADMIN".equals(roleCode)) {
            return new boolean[]{true, true, true, true, true, true, true, true};
        }
        
        if ("ENTERPRISE_HR".equals(roleCode)) {
            switch (moduleIndex) {
                case 0: return new boolean[]{true, true, true, true, true, true, true, true};
                case 1: return new boolean[]{true, false, false, false, false, true, true, true};
                case 2: return new boolean[]{false, false, false, true, true, true, true, true};
                case 3: return new boolean[]{false, false, false, false, false, false};
                case 4: return new boolean[]{false, true, false, false, true, false};
                default: return new boolean[]{};
            }
        }
        
        if ("JOB_SEEKER".equals(roleCode)) {
            switch (moduleIndex) {
                case 0: return new boolean[]{true, true, false, false, false, false, false, false};
                case 1: return new boolean[]{true, true, true, true, true, false, false, false};
                case 2: return new boolean[]{true, true, true, false, false, false, false, false};
                case 3: return new boolean[]{true, true, true, true, false, false};
                case 4: return new boolean[]{false, false, false, false, false, false};
                default: return new boolean[]{};
            }
        }
        
        return new boolean[]{};
    }
    
    @Override
    public RoleResponse getRoleByCode(String roleCode) {
        Role role = roleRepository.findByRoleCodeWithPermissions(roleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        return convertToRoleResponse(role);
    }
    
    @Override
    public List<PermissionResponse> getUserPermissions(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserIdWithRoleAndPermissions(userId);
        
        Set<Permission> permissions = new HashSet<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getRole() != null && userRole.getRole().getPermissions() != null) {
                permissions.addAll(userRole.getRole().getPermissions());
            }
        }
        
        return permissions.stream()
                .filter(p -> p.getStatus() == 1)
                .map(this::convertToPermissionResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        Optional<UserRole> existing = userRoleRepository.findByUserIdAndRoleId(userId, roleId);
        if (existing.isPresent()) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRole(role);
        
        userRoleRepository.save(userRole);
    }
    
    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }
    
    @Override
    @Transactional
    public void updateUserPermissions(Long userId, Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        Set<Permission> permissions = new HashSet<>();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<Permission> permissionList = permissionRepository.findAllById(permissionIds);
            permissions.addAll(permissionList);
        }
        
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        Set<Permission> permissions = new HashSet<>();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<Permission> permissionList = permissionRepository.findAllById(permissionIds);
            permissions.addAll(permissionList);
        }

        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateRolePermissionsByCode(String roleCode, List<Long> permissionIds) {
        Role role = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        Set<Permission> permissions = new HashSet<>();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<Permission> permissionList = permissionRepository.findAllById(permissionIds);
            permissions.addAll(permissionList);
        }

        role.setPermissions(permissions);
        roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void createRole(String roleCode, String roleName, String description) {
        if (roleRepository.findByRoleCode(roleCode).isPresent()) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        
        Role role = new Role();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setStatus(1);
        
        roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void updateRole(Long roleId, String roleName, String description) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        role.setRoleName(roleName);
        role.setDescription(description);
        
        roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        role.setStatus(0);
        roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void createPermission(String permissionCode, String permissionName, String module, String description) {
        if (permissionRepository.findByPermissionCode(permissionCode).isPresent()) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        
        Permission permission = new Permission();
        permission.setPermissionCode(permissionCode);
        permission.setPermissionName(permissionName);
        permission.setModule(module);
        permission.setDescription(description);
        permission.setStatus(1);
        
        permissionRepository.save(permission);
    }
    
    @Override
    @Transactional
    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        permission.setStatus(0);
        permissionRepository.save(permission);
    }
    
    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<PermissionResponse> permissions = getUserPermissions(userId);
        return permissions.stream()
                .anyMatch(p -> p.getPermissionCode().equals(permissionCode));
    }
    
@Override
    public boolean hasRole(Long userId, String roleCode) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream()
                .anyMatch(ur -> ur.getRole() != null && roleCode.equals(ur.getRole().getRoleCode()));
    }

    private PermissionResponse convertToPermissionResponse(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setPermissionCode(permission.getPermissionCode());
        response.setPermissionName(permission.getPermissionName());
        response.setModule(permission.getModule());
        response.setDescription(permission.getDescription());
        response.setStatus(permission.getStatus());
        response.setCreateTime(permission.getCreateTime());
        return response;
    }
    
    private RoleResponse convertToRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setStatus(role.getStatus());
        response.setCreateTime(role.getCreateTime());
        
        if (role.getPermissions() != null) {
            List<PermissionResponse> permissions = role.getPermissions().stream()
                    .filter(p -> p.getStatus() == 1)
                    .map(this::convertToPermissionResponse)
                    .collect(Collectors.toList());
            response.setPermissions(permissions);
        }
        
        return response;
    }
}
