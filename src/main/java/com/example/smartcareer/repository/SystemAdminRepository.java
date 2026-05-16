package com.example.smartcareer.repository;

import com.example.smartcareer.entity.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Long> {
    
    List<SystemAdmin> findByPermissionLevel(String permissionLevel);
    
    List<SystemAdmin> findByStatus(Integer status);
}
