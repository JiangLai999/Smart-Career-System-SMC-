package com.example.smartcareer.service.impl;

import com.example.smartcareer.entity.Announcement;
import com.example.smartcareer.entity.Notification;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.AnnouncementRepository;
import com.example.smartcareer.repository.NotificationRepository;
import com.example.smartcareer.repository.UserRepository;
import com.example.smartcareer.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    
    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public Announcement createAnnouncement(Announcement announcement) {
        announcement.setIsPublished(0);
        announcement.setViewCount(0);
        return announcementRepository.save(announcement);
    }
    
    @Override
    @Transactional
    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        Announcement existing = getAnnouncementById(id);
        
        if (announcement.getTitle() != null) {
            existing.setTitle(announcement.getTitle());
        }
        if (announcement.getContent() != null) {
            existing.setContent(announcement.getContent());
        }
        if (announcement.getType() != null) {
            existing.setType(announcement.getType());
        }
        if (announcement.getTargetAudience() != null) {
            existing.setTargetAudience(announcement.getTargetAudience());
        }
        if (announcement.getExpireTime() != null) {
            existing.setExpireTime(announcement.getExpireTime());
        }
        if (announcement.getPriority() != null) {
            existing.setPriority(announcement.getPriority());
        }
        
        return announcementRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = getAnnouncementById(id);
        announcementRepository.delete(announcement);
    }
    
    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "公告不存在"));
    }
    
    @Override
    @Transactional
    public Announcement publishAnnouncement(Long id, Long adminId) {
        Announcement announcement = getAnnouncementById(id);
        
        announcement.setIsPublished(1);
        announcement.setPublishTime(LocalDateTime.now());
        announcement.setAdminId(adminId);
        
        Announcement saved = announcementRepository.save(announcement);
        
        try {
            sendAnnouncementNotifications(saved);
        } catch (Exception e) {
            System.err.println("发送公告通知失败: " + e.getMessage());
        }
        
        return saved;
    }
    
    @Override
    @Transactional
    public Announcement unpublishAnnouncement(Long id) {
        Announcement announcement = getAnnouncementById(id);
        announcement.setIsPublished(0);
        return announcementRepository.save(announcement);
    }
    
    @Override
    public Page<Announcement> getAnnouncementList(String type, Integer isPublished, String targetAudience, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return announcementRepository.findByFilters(type, isPublished, targetAudience, pageable);
    }
    
    @Override
    public List<Announcement> getActiveAnnouncements(String audience) {
        return announcementRepository.findActiveAnnouncements(LocalDateTime.now(), audience);
    }
    
    @Override
    public Page<Announcement> getActiveAnnouncements(String audience, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        return announcementRepository.findActiveAnnouncements(LocalDateTime.now(), audience, pageable);
    }
    
    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        announcementRepository.incrementViewCount(id);
    }
    
    @Override
    public Map<String, Object> getAnnouncementStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Announcement> all = announcementRepository.findAll();
        Long activeCount = announcementRepository.countActiveAnnouncements(LocalDateTime.now());
        
        long totalCount = all.size();
        long publishedCount = all.stream().filter(a -> a.getIsPublished() == 1).count();
        long draftCount = all.stream().filter(a -> a.getIsPublished() == 0).count();
        
        stats.put("totalCount", totalCount);
        stats.put("publishedCount", publishedCount);
        stats.put("draftCount", draftCount);
        stats.put("activeCount", activeCount);
        
        return stats;
    }
    
    private void sendAnnouncementNotifications(Announcement announcement) {
        try {
            String targetAudience = announcement.getTargetAudience();

            List<com.example.smartcareer.entity.User> users;

            if ("ALL".equals(targetAudience)) {
                users = userRepository.findAll();
            } else if ("JOB_SEEKER".equals(targetAudience)) {
                users = userRepository.findByUserType("JOB_SEEKER");
            } else if ("ENTERPRISE".equals(targetAudience)) {
                // 企业端用户的 userType 是 ENTERPRISE_HR
                users = userRepository.findByUserType("ENTERPRISE_HR");
            } else {
                users = userRepository.findAll();
            }

            System.out.println("发送公告通知: targetAudience=" + targetAudience + ", 找到用户数=" + users.size());

            for (com.example.smartcareer.entity.User user : users) {
                if (user != null && !"SYSTEM_ADMIN".equals(user.getUserType())) {
                    Notification notification = new Notification();
                    notification.setUserId(user.getId());
                    notification.setType("ANNOUNCEMENT");
                    notification.setTitle("系统公告: " + announcement.getTitle());
                    notification.setContent(announcement.getContent());
                    notification.setRelatedId(announcement.getId());
                    notification.setRelatedType("ANNOUNCEMENT");
                    notification.setIsRead(0);
                    notificationRepository.save(notification);
                    System.out.println("已发送公告通知给用户: userId=" + user.getId() + ", userType=" + user.getUserType());
                }
            }
        } catch (Exception e) {
            System.err.println("发送公告通知失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}