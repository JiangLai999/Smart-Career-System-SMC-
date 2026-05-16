package com.example.smartcareer.service;

import com.example.smartcareer.entity.Announcement;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    
    Announcement createAnnouncement(Announcement announcement);
    
    Announcement updateAnnouncement(Long id, Announcement announcement);
    
    void deleteAnnouncement(Long id);
    
    Announcement getAnnouncementById(Long id);
    
    Announcement publishAnnouncement(Long id, Long adminId);
    
    Announcement unpublishAnnouncement(Long id);
    
    Page<Announcement> getAnnouncementList(String type, Integer isPublished, String targetAudience, int page, int size);
    
    List<Announcement> getActiveAnnouncements(String audience);
    
    Page<Announcement> getActiveAnnouncements(String audience, int page, int size);
    
    void incrementViewCount(Long id);
    
    Map<String, Object> getAnnouncementStatistics();
}