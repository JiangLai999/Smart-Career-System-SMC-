package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    List<Interview> findByJobSeekerId(Long jobSeekerId);
    
    Page<Interview> findByJobSeekerId(Long jobSeekerId, Pageable pageable);
    
    List<Interview> findByJobId(Long jobId);
    
    List<Interview> findByEnterpriseId(Long enterpriseId);
    
    Page<Interview> findByEnterpriseId(Long enterpriseId, Pageable pageable);
    
    List<Interview> findByApplicationId(Long applicationId);
    
    List<Interview> findByStatus(String status);
    
    @Query("SELECT i FROM Interview i WHERE i.jobSeeker.id = :jobSeekerId AND i.status = :status")
    List<Interview> findByJobSeekerAndStatus(@Param("jobSeekerId") Long jobSeekerId, @Param("status") String status);
    
    @Query("SELECT i FROM Interview i WHERE i.enterprise.id = :enterpriseId AND i.status = :status")
    Page<Interview> findByEnterpriseAndStatus(@Param("enterpriseId") Long enterpriseId, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT i FROM Interview i WHERE i.interviewTime BETWEEN :startTime AND :endTime")
    List<Interview> findByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    long countByJobSeekerIdAndStatus(Long jobSeekerId, String status);
}
