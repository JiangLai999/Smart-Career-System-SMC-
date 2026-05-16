package com.example.smartcareer.repository;

import com.example.smartcareer.entity.SettingChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SettingChangeLogRepository extends JpaRepository<SettingChangeLog, Long> {
    
    List<SettingChangeLog> findBySettingKeyOrderByChangeTimeDesc(String settingKey);
    
    Page<SettingChangeLog> findByChangeTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    Page<SettingChangeLog> findByOperatorId(Long operatorId, Pageable pageable);
    
    Page<SettingChangeLog> findBySettingKeyContaining(String keyword, Pageable pageable);
    
    List<SettingChangeLog> findTop10ByOrderByChangeTimeDesc();
    
    Page<SettingChangeLog> findBySettingId(Long settingId, Pageable pageable);
}