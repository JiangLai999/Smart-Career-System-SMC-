package com.example.smartcareer.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class RoleResponse {
    
    private Long id;
    
    private String roleCode;
    
    private String roleName;
    
    private String description;
    
    private List<PermissionResponse> permissions;
    
    private Integer status;
    
    private Integer userCount;
    
    private LocalDateTime createTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<PermissionResponse> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getUserCount() {
        return userCount;
    }
    
    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
