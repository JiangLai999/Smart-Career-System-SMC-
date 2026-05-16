package com.example.smartcareer.repository;

import com.example.smartcareer.entity.SystemSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    
    Optional<SystemSetting> findBySettingKey(String settingKey);
    
    List<SystemSetting> findByCategory(String category);
    
    Page<SystemSetting> findByCategory(String category, Pageable pageable);
    
    List<SystemSetting> findBySettingType(String settingType);
    
    boolean existsBySettingKey(String settingKey);
}