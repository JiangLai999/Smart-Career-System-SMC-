package com.example.smartcareer.repository;

import com.example.smartcareer.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    List<UserRole> findByUserId(Long userId);
    
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role r JOIN FETCH r.permissions WHERE ur.userId = :userId")
    List<UserRole> findByUserIdWithRoleAndPermissions(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId AND ur.role.id = :roleId")
    void deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    int countByRoleId(Long roleId);
}
