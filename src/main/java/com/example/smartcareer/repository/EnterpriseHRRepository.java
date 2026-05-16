package com.example.smartcareer.repository;

import com.example.smartcareer.entity.EnterpriseHR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseHRRepository extends JpaRepository<EnterpriseHR, Long> {

    Optional<EnterpriseHR> findById(Long id);

    List<EnterpriseHR> findByEnterpriseId(Long enterpriseId);

    List<EnterpriseHR> findByAuditStatus(Integer auditStatus);

    List<EnterpriseHR> findByStatus(Integer status);
    
    Optional<EnterpriseHR> findByUsername(String username);
    
    Optional<EnterpriseHR> findByPhone(String phone);
    
    Optional<EnterpriseHR> findByEmail(String email);
    
    @Query(value = "SELECT e.enterprise_name FROM enterprise e " +
            "INNER JOIN enterprise_hr eh ON e.id = eh.enterprise_id " +
            "INNER JOIN users u ON eh.id = u.id " +
            "WHERE eh.id = :hrId AND u.dtype IN ('EnterpriseHR', 'ENTERPRISE_HR')", nativeQuery = true)
    String findEnterpriseNameByHrId(@Param("hrId") Long hrId);
    
@Query(value = "SELECT u.id, u.username, u.email, u.phone, u.user_type, u.status, u.create_time, " +
  "eh.enterprise_id, e.enterprise_name " +
  "FROM users u " +
  "LEFT JOIN enterprise_hr eh ON u.id = eh.id " +
  "LEFT JOIN enterprise e ON eh.enterprise_id = e.id " +
  "WHERE u.dtype IN ('EnterpriseHR', 'ENTERPRISE_HR')", nativeQuery = true)
List<Object[]> findAllEnterpriseHRWithEnterprise();

@Query(value = "SELECT eh.enterprise_id FROM enterprise_hr eh WHERE eh.id = :hrId", nativeQuery = true)
Long findEnterpriseIdByHrId(@Param("hrId") Long hrId);
}
