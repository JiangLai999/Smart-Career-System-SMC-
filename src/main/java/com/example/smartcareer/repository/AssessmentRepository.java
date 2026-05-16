package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    
    List<Assessment> findByJobSeekerId(Long jobSeekerId);
    
    List<Assessment> findByAssessmentType(String assessmentType);
    
    @Query("SELECT a FROM Assessment a WHERE a.jobSeeker.id = :jobSeekerId ORDER BY a.createTime DESC")
    List<Assessment> findByJobSeekerIdOrderByTime(@Param("jobSeekerId") Long jobSeekerId);
    
    @Query("SELECT a FROM Assessment a WHERE a.jobSeeker.id = :jobSeekerId ORDER BY a.createTime DESC")
    Page<Assessment> findByJobSeekerIdOrderByTime(@Param("jobSeekerId") Long jobSeekerId, Pageable pageable);
    
    @Query("SELECT a FROM Assessment a WHERE a.jobSeeker.id = :jobSeekerId AND a.assessmentType = :type ORDER BY a.createTime DESC")
    List<Assessment> findByJobSeekerAndType(@Param("jobSeekerId") Long jobSeekerId, @Param("type") String type);
}
