package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enterprise")
public class Enterprise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String enterpriseName;
    
    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;
    
    @Column(length = 50)
    private String industry;
    
    @Column(length = 20)
    private String scale;
    
    @Column(length = 50)
    private String contactPerson;
    
    @Column(length = 20)
    private String contactPhone;
    
    @Column(length = 100)
    private String contactEmail;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String culture;
    
    @Column(length = 200)
    private String address;
    
    @Column(length = 100)
    private String logo;
    
    @Column(length = 200)
    private String website;
    
    @Column(nullable = false)
    private Integer auditStatus = 0;
    
    private LocalDateTime auditTime;
    
    @Column(length = 500)
    private String auditComment;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer status = 1;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
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
    
    public String getEnterpriseName() {
        return enterpriseName;
    }
    
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getScale() {
        return scale;
    }
    
    public void setScale(String scale) {
        this.scale = scale;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCulture() {
        return culture;
    }
    
    public void setCulture(String culture) {
        this.culture = culture;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
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
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getName() {
        return enterpriseName;
    }
}
