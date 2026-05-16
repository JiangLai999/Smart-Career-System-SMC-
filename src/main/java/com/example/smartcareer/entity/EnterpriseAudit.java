package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enterprise_audit")
public class EnterpriseAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private SystemAdmin admin;
    
    @Column(columnDefinition = "TEXT")
    private String submittedData;
    
    @Column(nullable = false, length = 20)
    private String auditStatus = "PENDING";
    
    @Column(columnDefinition = "TEXT")
    private String auditComment;
    
    @Column(nullable = false)
    private LocalDateTime submitTime;
    
    private LocalDateTime auditTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        submitTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Enterprise getEnterprise() {
        return enterprise;
    }
    
    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
    
    public SystemAdmin getAdmin() {
        return admin;
    }
    
    public void setAdmin(SystemAdmin admin) {
        this.admin = admin;
    }
    
    public String getSubmittedData() {
        return submittedData;
    }
    
    public void setSubmittedData(String submittedData) {
        this.submittedData = submittedData;
    }
    
    public String getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public String getAuditComment() {
        return auditComment;
    }
    
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
    
    public LocalDateTime getSubmitTime() {
        return submitTime;
    }
    
    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }
    
    public LocalDateTime getAuditTime() {
        return auditTime;
    }
    
    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
