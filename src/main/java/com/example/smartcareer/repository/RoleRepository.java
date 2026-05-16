package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByRoleCode(String roleCode);
    
    Optional<Role> findByRoleName(String roleName);
    
    boolean existsByRoleName(String roleName);
    
    List<Role> findByStatus(Integer status);
    
    @Query("SELECT r FROM Role r WHERE r.status = 1")
    List<Role> findAllActive();
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.id = :roleId")
    Optional<Role> findByIdWithPermissions(@Param("roleId") Long roleId);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.roleCode = :roleCode AND r.status = 1")
    Optional<Role> findByRoleCodeWithPermissions(@Param("roleCode") String roleCode);
}
