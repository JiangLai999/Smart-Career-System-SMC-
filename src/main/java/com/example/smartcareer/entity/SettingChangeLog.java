package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "setting_change_log")
public class SettingChangeLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private SystemSetting setting;
    
    @Column(nullable = false, length = 100)
    private String settingKey;
    
    @Column(columnDefinition = "TEXT")
    private String oldValue;
    
    @Column(columnDefinition = "TEXT")
    private String newValue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    private SystemAdmin operator;
    
    @Column(nullable = false, length = 50)
    private String operatorName;
    
    @Column(length = 200)
    private String changeReason;
    
    @Column(nullable = false)
    private LocalDateTime changeTime;
    
    @PrePersist
    protected void onCreate() {
        changeTime = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public SystemSetting getSetting() {
        return setting;
    }
    
    public void setSetting(SystemSetting setting) {
        this.setting = setting;
    }
    
    public String getSettingKey() {
        return settingKey;
    }
    
    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public SystemAdmin getOperator() {
        return operator;
    }
    
    public void setOperator(SystemAdmin operator) {
        this.operator = operator;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public String getChangeReason() {
        return changeReason;
    }
    
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
    
    public LocalDateTime getChangeTime() {
        return changeTime;
    }
    
    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }
}