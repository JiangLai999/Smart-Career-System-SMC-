package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByPermissionCode(String permissionCode);
    
    List<Permission> findByModule(String module);
    
    List<Permission> findByStatus(Integer status);
    
    @Query("SELECT p FROM Permission p WHERE p.status = 1 ORDER BY p.module, p.id")
    List<Permission> findAllActive();
    
    @Query("SELECT DISTINCT p.module FROM Permission p WHERE p.status = 1")
    List<String> findAllModules();
    
    @Query("SELECT p FROM Permission p WHERE p.module = :module AND p.status = 1 ORDER BY p.id")
    List<Permission> findActiveByModule(@Param("module") String module);
}
