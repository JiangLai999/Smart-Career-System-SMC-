package com.example.smartcareer.repository;

import com.example.smartcareer.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    List<Announcement> findByIsPublishedOrderByPublishTimeDesc(Integer isPublished);
    
    Page<Announcement> findByIsPublishedOrderByPublishTimeDesc(Integer isPublished, Pageable pageable);
    
    @Query("SELECT a FROM Announcement a WHERE a.isPublished = 1 " +
           "AND (a.expireTime IS NULL OR a.expireTime > :now) " +
           "AND (a.targetAudience = 'ALL' OR a.targetAudience = :audience) " +
           "ORDER BY a.priority DESC, a.publishTime DESC")
    List<Announcement> findActiveAnnouncements(@Param("now") LocalDateTime now, @Param("audience") String audience);
    
    @Query("SELECT a FROM Announcement a WHERE a.isPublished = 1 " +
           "AND (a.expireTime IS NULL OR a.expireTime > :now) " +
           "AND (a.targetAudience = 'ALL' OR a.targetAudience = :audience) " +
           "ORDER BY a.priority DESC, a.publishTime DESC")
    Page<Announcement> findActiveAnnouncements(@Param("now") LocalDateTime now, @Param("audience") String audience, Pageable pageable);
    
    @Query("SELECT a FROM Announcement a WHERE " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:isPublished IS NULL OR a.isPublished = :isPublished) AND " +
           "(:targetAudience IS NULL OR a.targetAudience = :targetAudience) " +
           "ORDER BY a.createTime DESC")
    Page<Announcement> findByFilters(
        @Param("type") String type,
        @Param("isPublished") Integer isPublished,
        @Param("targetAudience") String targetAudience,
        Pageable pageable
    );
    
    @Modifying
    @Query("UPDATE Announcement a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    @Query("SELECT COUNT(a) FROM Announcement a WHERE a.isPublished = 1 AND (a.expireTime IS NULL OR a.expireTime > :now)")
    Long countActiveAnnouncements(@Param("now") LocalDateTime now);
}