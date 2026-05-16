package com.example.smartcareer.repository;

import com.example.smartcareer.entity.ResumeScreening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeScreeningRepository extends JpaRepository<ResumeScreening, Long> {
    
    List<ResumeScreening> findByJobId(Long jobId);
    
    List<ResumeScreening> findByApplicationId(Long applicationId);
    
    List<ResumeScreening> findByScreeningResult(String result);
    
    List<ResumeScreening> findByHrId(Long hrId);
}
