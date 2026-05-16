package com.example.smartcareer.repository;

import com.example.smartcareer.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    
    Optional<JobSeeker> findById(Long id);
    
    List<JobSeeker> findByExpectedPositionContaining(String position);
    
    List<JobSeeker> findByExpectedLocationContaining(String location);
    
    @Query("SELECT js FROM JobSeeker js WHERE js.expectedSalaryMin >= :min AND js.expectedSalaryMax <= :max")
    List<JobSeeker> findBySalaryRange(@Param("min") Integer min, @Param("max") Integer max);
    
    @Query("SELECT js FROM JobSeeker js WHERE js.skills LIKE %:skill%")
    List<JobSeeker> findBySkill(@Param("skill") String skill);
    
    List<JobSeeker> findByEducation(String education);
    
    List<JobSeeker> findByStatus(Integer status);
}
