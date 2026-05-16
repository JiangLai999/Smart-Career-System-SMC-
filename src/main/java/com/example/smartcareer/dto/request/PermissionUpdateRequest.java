package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PermissionUpdateRequest {
    
    private Long roleId;
    
    private String roleCode;
    
    @NotNull(message = "权限ID列表不能为空")
    private List<Long> permissionIds;
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public List<Long> getPermissionIds() {
        return permissionIds;
    }
    
    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
