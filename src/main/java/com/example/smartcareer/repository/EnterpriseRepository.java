package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Enterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    
    Optional<Enterprise> findByEnterpriseName(String enterpriseName);
    
    List<Enterprise> findByIndustry(String industry);
    
    List<Enterprise> findByAuditStatus(Integer auditStatus);
    
    List<Enterprise> findByStatus(Integer status);
    
    Page<Enterprise> findByAuditStatusAndStatus(Integer auditStatus, Integer status, Pageable pageable);
    
    @Query("SELECT e FROM Enterprise e WHERE e.enterpriseName LIKE %:name%")
    List<Enterprise> searchByName(@Param("name") String name);
    
    @Query("SELECT e FROM Enterprise e WHERE e.industry LIKE %:industry%")
    List<Enterprise> searchByIndustry(@Param("industry") String industry);
    
    boolean existsByLicenseNumber(String licenseNumber);
}
