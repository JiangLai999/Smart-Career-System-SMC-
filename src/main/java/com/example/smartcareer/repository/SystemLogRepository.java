package com.example.smartcareer.repository;

import com.example.smartcareer.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    
    List<SystemLog> findByUserId(Long userId);
    
    List<SystemLog> findByOperationType(String operationType);
    
    Page<SystemLog> findByOperationType(String operationType, Pageable pageable);
    
    List<SystemLog> findByUserType(String userType);
    
    @Query("SELECT sl FROM SystemLog sl WHERE sl.operationTime BETWEEN :startTime AND :endTime ORDER BY sl.operationTime DESC")
    List<SystemLog> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT sl FROM SystemLog sl WHERE sl.userId = :userId AND sl.operationTime BETWEEN :startTime AND :endTime")
    List<SystemLog> findByUserAndTimeRange(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    Page<SystemLog> findAllByOrderByOperationTimeDesc(Pageable pageable);
    
    @Query("SELECT sl FROM SystemLog sl WHERE sl.operationType LIKE %:keyword% OR sl.operationDetail LIKE %:keyword% OR sl.username LIKE %:keyword%")
    Page<SystemLog> searchLogs(@Param("keyword") String keyword, Pageable pageable);
    
    @Modifying
    @Query("DELETE FROM SystemLog sl WHERE sl.operationTime < :beforeTime")
    int deleteOldLogs(@Param("beforeTime") LocalDateTime beforeTime);
}
