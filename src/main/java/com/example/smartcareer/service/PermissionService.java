package com.example.smartcareer.service;

import com.example.smartcareer.dto.response.PermissionResponse;
import com.example.smartcareer.dto.response.RoleResponse;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

    List<PermissionResponse> getPermissionsByModule(String module);

    List<String> getAllModules();

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleByCode(String roleCode);

    List<PermissionResponse> getUserPermissions(Long userId);

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

    void updateUserPermissions(Long userId, Long roleId, List<Long> permissionIds);

    void updateRolePermissions(Long roleId, List<Long> permissionIds);

    void updateRolePermissionsByCode(String roleCode, List<Long> permissionIds);

    void createRole(String roleCode, String roleName, String description);

    void updateRole(Long roleId, String roleName, String description);

    void deleteRole(Long roleId);

    void createPermission(String permissionCode, String permissionName, String module, String description);

    void deletePermission(Long permissionId);

    boolean hasPermission(Long userId, String permissionCode);

    boolean hasRole(Long userId, String roleCode);

    Map<String, Object> getRolePermissionsByCode(String roleCode);
}
