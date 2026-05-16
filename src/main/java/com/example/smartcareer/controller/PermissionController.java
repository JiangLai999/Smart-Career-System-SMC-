package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.PermissionUpdateRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.PermissionResponse;
import com.example.smartcareer.dto.response.RoleResponse;
import com.example.smartcareer.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    @GetMapping("/list")
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ApiResponse.success(permissions);
    }
    
    @GetMapping("/module/{module}")
    public ApiResponse<List<PermissionResponse>> getPermissionsByModule(@PathVariable String module) {
        List<PermissionResponse> permissions = permissionService.getPermissionsByModule(module);
        return ApiResponse.success(permissions);
    }
    
    @GetMapping("/modules")
    public ApiResponse<List<String>> getAllModules() {
        List<String> modules = permissionService.getAllModules();
        return ApiResponse.success(modules);
    }
    
    @GetMapping("/role/list")
public ApiResponse<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = permissionService.getAllRoles();
        return ApiResponse.success(roles);
    }

    @GetMapping("/role/code/{roleCode}")
    public ApiResponse<RoleResponse> getRoleByCode(@PathVariable String roleCode) {
        RoleResponse role = permissionService.getRoleByCode(roleCode);
        return ApiResponse.success(role);
    }
    
    @GetMapping("/user/{userId}")
    public ApiResponse<List<PermissionResponse>> getUserPermissions(@PathVariable Long userId) {
        List<PermissionResponse> permissions = permissionService.getUserPermissions(userId);
        return ApiResponse.success(permissions);
    }
    
    @PostMapping("/user/{userId}/role/{roleId}")
    public ApiResponse<String> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        permissionService.assignRoleToUser(userId, roleId);
        return ApiResponse.success("角色分配成功");
    }
    
    @DeleteMapping("/user/{userId}/role/{roleId}")
    public ApiResponse<String> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        permissionService.removeRoleFromUser(userId, roleId);
        return ApiResponse.success("角色移除成功");
    }
    
    @PutMapping("/update")
    public ApiResponse<String> updateUserPermissions(@RequestBody PermissionUpdateRequest request) {
        if (request.getRoleCode() != null) {
            permissionService.updateRolePermissionsByCode(request.getRoleCode(), request.getPermissionIds());
        } else if (request.getRoleId() != null) {
            permissionService.updateRolePermissions(request.getRoleId(), request.getPermissionIds());
        } else {
            return ApiResponse.error("角色ID或角色代码不能为空");
        }
        return ApiResponse.success("权限更新成功");
    }
    
    @PostMapping("/role/create")
    public ApiResponse<String> createRole(@RequestBody Map<String, String> request) {
        String roleCode = request.get("roleCode");
        String roleName = request.get("roleName");
        String description = request.get("description");
        permissionService.createRole(roleCode, roleName, description);
        return ApiResponse.success("角色创建成功");
    }
    
    @PutMapping("/role/{roleId}")
    public ApiResponse<String> updateRole(
            @PathVariable Long roleId,
            @RequestBody Map<String, String> request) {
        String roleName = request.get("roleName");
        String description = request.get("description");
        permissionService.updateRole(roleId, roleName, description);
        return ApiResponse.success("角色更新成功");
    }
    
    @DeleteMapping("/role/{roleId}")
    public ApiResponse<String> deleteRole(@PathVariable Long roleId) {
        permissionService.deleteRole(roleId);
        return ApiResponse.success("角色删除成功");
    }
    
    @PostMapping("/create")
    public ApiResponse<String> createPermission(@RequestBody Map<String, String> request) {
        String permissionCode = request.get("permissionCode");
        String permissionName = request.get("permissionName");
        String module = request.get("module");
        String description = request.get("description");
        permissionService.createPermission(permissionCode, permissionName, module, description);
        return ApiResponse.success("权限创建成功");
    }
    
    @DeleteMapping("/{permissionId}")
    public ApiResponse<String> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse.success("权限删除成功");
    }
    
    @GetMapping("/check/{userId}/{permissionCode}")
    public ApiResponse<Boolean> hasPermission(
            @PathVariable Long userId,
            @PathVariable String permissionCode) {
        boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
        return ApiResponse.success(hasPermission);
    }
    
    @GetMapping("/check-role/{userId}/{roleCode}")
    public ApiResponse<Boolean> hasRole(
            @PathVariable Long userId,
            @PathVariable String roleCode) {
        boolean hasRole = permissionService.hasRole(userId, roleCode);
        return ApiResponse.success(hasRole);
    }
    
    @GetMapping("/role/{roleId}/permissions")
    public ApiResponse<Map<String, Object>> getRolePermissions(@PathVariable String roleId) {
        Map<String, Object> permissions = permissionService.getRolePermissionsByCode(roleId);
        return ApiResponse.success(permissions);
    }
}
