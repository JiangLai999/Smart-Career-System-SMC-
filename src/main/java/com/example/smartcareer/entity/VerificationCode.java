package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_code")
public class VerificationCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 20, nullable = false)
    private String phone;
    
    @Column(length = 10, nullable = false)
    private String code;
    
    @Column(length = 20)
    private String purpose;
    
    @Column(nullable = false)
    private LocalDateTime expireTime;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private Integer used = 0;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public Integer getUsed() {
        return used;
    }
    
    public void setUsed(Integer used) {
        this.used = used;
    }
}
