package com.example.smartcareer.repository;

import com.example.smartcareer.entity.ResumeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeFilterRepository extends JpaRepository<ResumeFilter, Long> {
    
    Page<ResumeFilter> findByEnterpriseIdOrderByCreateTimeDesc(Long enterpriseId, Pageable pageable);
    
    List<ResumeFilter> findByEnterpriseIdOrderByCreateTimeDesc(Long enterpriseId);
    
    void deleteByEnterpriseIdAndId(Long enterpriseId, Long id);
}
