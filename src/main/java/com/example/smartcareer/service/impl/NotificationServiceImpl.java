package com.example.smartcareer.service.impl;

import com.example.smartcareer.entity.Notification;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.NotificationRepository;
import com.example.smartcareer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Override
    @Transactional
    public Notification createNotification(Long userId, String type, String title, String content, Long relatedId, String relatedType) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setRelatedType(relatedType);
        notification.setIsRead(0);
        
        return notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public Notification sendHireNotification(Long userId, Long applicationId, String jobTitle, String companyName) {
        String title = "录用通知";
        String content = String.format("恭喜您！%s已向您发送录用通知，职位：%s。请及时查看并回复。", companyName, jobTitle);
        
        return createNotification(userId, "HIRE", title, content, applicationId, "APPLICATION");
    }
    
    @Override
    @Transactional
    public Notification sendInterviewNotification(Long userId, Long interviewId, String jobTitle, String interviewTime, String location) {
        String title = "面试邀请";
        String content = String.format("您收到新的面试邀请！职位：%s，面试时间：%s，地点：%s。请及时确认。", 
                jobTitle, interviewTime, location);
        
        return createNotification(userId, "INTERVIEW", title, content, interviewId, "INTERVIEW");
    }
    
    @Override
    @Transactional
    public Notification sendRejectNotification(Long userId, Long applicationId, String jobTitle, String companyName) {
        String title = "申请结果通知";
        String content = String.format("感谢您对%s的%s职位投递简历。经过综合评估，我们很遗憾地通知您暂不符合该职位要求。", 
                companyName, jobTitle);
        
        return createNotification(userId, "REJECT", title, content, applicationId, "APPLICATION");
    }
    
    @Override
    @Transactional
    public Notification sendWarningNotification(Long userId, String reason) {
        String title = "系统警告";
        String content = String.format("您收到一条系统警告。原因：%s。请遵守平台规则，如有疑问请联系客服。", reason);
        
        return createNotification(userId, "WARNING", title, content, null, null);
    }
    
    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    }
    
    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }
    
    @Override
    public Page<Notification> getUserNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return notificationRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
    }
    
    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreateTimeDesc(userId, 0);
    }
    
    @Override
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
    
    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }
    
    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
    
    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    @Override
    public Map<String, Object> getNotificationStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();
        
        List<Notification> allNotifications = notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
        Long unreadCount = notificationRepository.countUnreadByUserId(userId);
        
        statistics.put("totalCount", allNotifications.size());
        statistics.put("unreadCount", unreadCount);
        
        long hireCount = allNotifications.stream().filter(n -> "HIRE".equals(n.getType())).count();
        long interviewCount = allNotifications.stream().filter(n -> "INTERVIEW".equals(n.getType())).count();
        long rejectCount = allNotifications.stream().filter(n -> "REJECT".equals(n.getType())).count();
        long warningCount = allNotifications.stream().filter(n -> "WARNING".equals(n.getType())).count();
        
        statistics.put("hireCount", hireCount);
        statistics.put("interviewCount", interviewCount);
        statistics.put("rejectCount", rejectCount);
        statistics.put("warningCount", warningCount);
        
        return statistics;
    }
}
