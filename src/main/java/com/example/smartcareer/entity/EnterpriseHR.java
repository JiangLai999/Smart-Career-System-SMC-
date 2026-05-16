package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ENTERPRISE_HR")
public class EnterpriseHR extends User {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;
    
    @Column(length = 50)
    private String realName;
    
    @Column(length = 50)
    private String position;
    
    @Column(length = 100)
    private String avatar;
    
    private Integer auditStatus = 0;
    
    private LocalDateTime auditTime;
    
    @Column(length = 500)
    private String auditComment;
    
    public EnterpriseHR() {
        this.setUserType("ENTERPRISE_HR");
    }
    
    public Enterprise getEnterprise() {
        return enterprise;
    }
    
    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Integer getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public LocalDateTime getAuditTime() {
        return auditTime;
    }
    
    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
    
    public String getAuditComment() {
        return auditComment;
    }
    
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
    
    public String getName() {
        return realName;
    }
}
