package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_filter")
public class ResumeFilter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long enterpriseId;
    
    @Column(nullable = false, length = 100)
    private String filterName;
    
    @Column(columnDefinition = "TEXT")
    private String filterJson;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
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
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    public String getFilterName() {
        return filterName;
    }
    
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
    
    public String getFilterJson() {
        return filterJson;
    }
    
    public void setFilterJson(String filterJson) {
        this.filterJson = filterJson;
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
}
