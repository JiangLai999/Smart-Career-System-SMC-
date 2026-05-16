package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Application;
import com.example.smartcareer.entity.Enterprise;
import com.example.smartcareer.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a JOIN FETCH a.jobSeeker JOIN FETCH a.job JOIN FETCH a.enterprise WHERE a.id = :id")
    Optional<Application> findByIdWithRelations(@Param("id") Long id);

    List<Application> findByJobSeekerId(Long jobSeekerId);

    Page<Application> findByJobSeekerId(Long jobSeekerId, Pageable pageable);

    List<Application> findByJobId(Long jobId);

    Page<Application> findByJobId(Long jobId, Pageable pageable);

    List<Application> findByEnterpriseId(Long enterpriseId);

    Page<Application> findByEnterpriseId(Long enterpriseId, Pageable pageable);

    List<Application> findByStatus(String status);

    List<Application> findByJobSeekerIdAndJobId(Long jobSeekerId, Long jobId);

    @Query("SELECT a FROM Application a WHERE a.jobSeeker.id = :jobSeekerId AND a.status = :status")
    List<Application> findByJobSeekerAndStatus(@Param("jobSeekerId") Long jobSeekerId, @Param("status") String status);

    @Query("SELECT a FROM Application a WHERE a.enterprise.id = :enterpriseId AND a.status = :status")
    Page<Application> findByEnterpriseAndStatus(@Param("enterpriseId") Long enterpriseId, @Param("status") String status, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.jobSeeker.id = :jobSeekerId")
    Long countByJobSeeker(@Param("jobSeekerId") Long jobSeekerId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.id = :jobId")
    Long countByJob(@Param("jobId") Long jobId);

    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId ORDER BY a.applyTime DESC")
    List<Application> findLatestApplicationsByJob(@Param("jobId") Long jobId);
    
    Page<Application> findByEnterprise(Enterprise enterprise, Pageable pageable);
    
    Page<Application> findByJob(Job job, Pageable pageable);
    
    long countByEnterprise(Enterprise enterprise);
    
    long countByEnterpriseAndStatus(Enterprise enterprise, String status);
    
    long countByJobSeekerId(Long jobSeekerId);

    long countByJobSeekerIdAndStatus(Long jobSeekerId, String status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.jobSeeker.id = :jobSeekerId AND a.applyTime > :since")
    long countByJobSeekerIdAndApplyTimeAfter(@Param("jobSeekerId") Long jobSeekerId, @Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM Application a WHERE a.enterprise.id = :enterpriseId AND a.status = :status ORDER BY a.applyTime DESC")
    List<Application> findByEnterpriseIdAndStatus(@Param("enterpriseId") Long enterpriseId, @Param("status") String status);
}
