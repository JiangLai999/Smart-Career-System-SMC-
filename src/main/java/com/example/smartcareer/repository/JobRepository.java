package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Enterprise;
import com.example.smartcareer.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByEnterpriseId(Long enterpriseId);
    
    List<Job> findByHrId(Long hrId);
    
    List<Job> findByStatus(String status);
    
    Page<Job> findByStatus(String status, Pageable pageable);
    
    Page<Job> findByStatusIn(List<String> statuses, Pageable pageable);
    
    List<Job> findByCategory(String category);
    
    List<Job> findByLocationContaining(String location);
    
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.enterprise WHERE j.id = :id")
    Optional<Job> findByIdWithEnterprise(@Param("id") Long id);
    
    @Query("SELECT j FROM Job j WHERE j.salaryMin >= :min AND j.salaryMax <= :max")
    List<Job> findBySalaryRange(@Param("min") Integer min, @Param("max") Integer max);
    
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:keyword% OR j.description LIKE %:keyword% OR j.requirement LIKE %:keyword%")
    Page<Job> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status AND (j.title LIKE %:keyword% OR j.description LIKE %:keyword%)")
    Page<Job> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status IN :statuses AND (j.title LIKE %:keyword% OR j.description LIKE %:keyword% OR j.requirement LIKE %:keyword%)")
    Page<Job> searchByKeywordAndStatusIn(@Param("keyword") String keyword, @Param("statuses") List<String> statuses, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Job j SET j.viewCount = j.viewCount + 1 WHERE j.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE Job j SET j.applyCount = j.applyCount + 1 WHERE j.id = :id")
    void incrementApplyCount(@Param("id") Long id);
    
    @Query("SELECT j FROM Job j WHERE j.enterprise.id = :enterpriseId AND j.status = 'PUBLISHED'")
    List<Job> findActiveJobsByEnterprise(@Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.enterprise.id = :enterpriseId")
    Long countByEnterprise(@Param("enterpriseId") Long enterpriseId);
    
    Page<Job> findByEnterprise(Enterprise enterprise, Pageable pageable);
    
    Page<Job> findByEnterpriseAndStatus(Enterprise enterprise, String status, Pageable pageable);
    
    long countByEnterpriseAndStatus(Enterprise enterprise, String status);
}
