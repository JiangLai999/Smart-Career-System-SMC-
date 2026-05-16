package com.example.smartcareer.repository;

import com.example.smartcareer.entity.EnterpriseAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnterpriseAuditRepository extends JpaRepository<EnterpriseAudit, Long> {
    
    List<EnterpriseAudit> findByEnterpriseId(Long enterpriseId);
    
    List<EnterpriseAudit> findByAuditStatus(String auditStatus);
    
    Page<EnterpriseAudit> findByAuditStatus(String auditStatus, Pageable pageable);
}
