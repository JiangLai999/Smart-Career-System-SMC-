package com.example.smartcareer.repository;

import com.example.smartcareer.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    
    Optional<VerificationCode> findByPhoneAndCodeAndPurpose(String phone, String code, String purpose);
    
    @Query("SELECT v FROM VerificationCode v WHERE v.phone = :phone AND v.purpose = :purpose AND v.expireTime > :now AND v.used = 0")
    Optional<VerificationCode> findValidCode(@Param("phone") String phone, @Param("purpose") String purpose, @Param("now") LocalDateTime now);
    
    @Modifying
    @Transactional
    @Query("UPDATE VerificationCode v SET v.used = 1 WHERE v.phone = :phone AND v.purpose = :purpose")
    void invalidateCodes(@Param("phone") String phone, @Param("purpose") String purpose);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationCode v WHERE v.expireTime < :now")
    void deleteExpiredCodes(@Param("now") LocalDateTime now);
}
