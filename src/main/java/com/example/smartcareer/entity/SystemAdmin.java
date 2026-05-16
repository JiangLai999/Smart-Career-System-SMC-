package com.example.smartcareer.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SYSTEM_ADMIN")
public class SystemAdmin extends User {
    
    @Column(length = 50)
    private String realName;
    
    @Column(length = 20)
    private String permissionLevel;
    
    @Column(length = 100)
    private String avatar;
    
    public SystemAdmin() {
        this.setUserType("SYSTEM_ADMIN");
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getPermissionLevel() {
        return permissionLevel;
    }
    
    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
