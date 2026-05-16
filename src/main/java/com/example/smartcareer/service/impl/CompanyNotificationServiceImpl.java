package com.example.smartcareer.service.impl;

import com.example.smartcareer.entity.Application;
import com.example.smartcareer.entity.Enterprise;
import com.example.smartcareer.entity.EnterpriseHR;
import com.example.smartcareer.entity.Interview;
import com.example.smartcareer.entity.Notification;
import com.example.smartcareer.entity.Announcement;
import com.example.smartcareer.repository.ApplicationRepository;
import com.example.smartcareer.repository.EnterpriseRepository;
import com.example.smartcareer.repository.EnterpriseHRRepository;
import com.example.smartcareer.repository.InterviewRepository;
import com.example.smartcareer.repository.NotificationRepository;
import com.example.smartcareer.repository.AnnouncementRepository;
import com.example.smartcareer.service.CompanyNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CompanyNotificationServiceImpl implements CompanyNotificationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Override
    public Page<Map<String, Object>> getNotifications(Long enterpriseId, Long userId, int page, int size) {
        List<Map<String, Object>> allNotifications = new ArrayList<>();

        if (enterpriseId == null && userId != null) {
            EnterpriseHR hr = enterpriseHRRepository.findById(userId).orElse(null);
            if (hr != null && hr.getEnterprise() != null) {
                enterpriseId = hr.getEnterprise().getId();
            }
        }

        Enterprise enterprise = null;
        if (enterpriseId != null) {
            enterprise = enterpriseRepository.findById(enterpriseId).orElse(null);
        }

        if (enterprise == null && userId == null) {
            return new PageImpl<>(allNotifications, PageRequest.of(page, size), 0);
        }

        // 1. 申请通知
        List<Application> applications = applicationRepository.findByEnterpriseId(enterpriseId);
        for (Application app : applications) {
            String seekerName = app.getJobSeeker() != null ? app.getJobSeeker().getRealName() : "求职者";
            String jobTitle   = app.getJob()       != null ? app.getJob().getTitle()           : "未知职位";
            String status     = app.getStatus();
            boolean isRead = "NOTIF_READ".equals(app.getTag()) || app.getViewTime() != null;

            Map<String, Object> notification = new HashMap<>();
            notification.put("id", 100000L + app.getId());
            notification.put("relatedId", app.getId());
            notification.put("relatedType", "APPLICATION");
            notification.put("isRead", isRead);
            notification.put("createTime", app.getApplyTime());
            notification.put("source", "USER");

            if ("APPLIED".equals(status) || "REVIEWING".equals(status)) {
                notification.put("type", "APPLICATION");
                notification.put("title", "新的简历投递");
                notification.put("content", "收到来自 " + seekerName + " 的简历，职位：" + jobTitle);
            } else if ("INTERVIEW".equals(status)) {
                notification.put("type", "INTERVIEW");
                notification.put("title", "面试已安排");
                notification.put("content", seekerName + " 的面试已安排，职位：" + jobTitle);
            } else if ("OFFERED".equals(status) || "HIRED".equals(status)) {
                notification.put("type", "HIRE");
                notification.put("title", "录用成功");
                notification.put("content", "已成功录用 " + seekerName + "，职位：" + jobTitle);
            } else if ("REJECTED".equals(status)) {
                notification.put("type", "REJECT");
                notification.put("title", "已拒绝申请");
                notification.put("content", "已拒绝 " + seekerName + " 的申请，职位：" + jobTitle);
            } else {
                notification.put("type", "APPLICATION");
                notification.put("title", "简历投递");
                notification.put("content", "来自 " + seekerName + " 的申请，职位：" + jobTitle);
            }
            allNotifications.add(notification);
        }

        // 2. 面试提醒
        List<Interview> interviews = interviewRepository.findByEnterpriseId(enterpriseId);
        for (Interview interview : interviews) {
            if (interview.getInterviewTime() != null
                    && interview.getInterviewTime().isAfter(LocalDateTime.now())
                    && interview.getInterviewTime().isBefore(LocalDateTime.now().plusDays(3))
                    && !"CANCELLED".equals(interview.getStatus())) {

                String seekerName = interview.getJobSeeker() != null ? interview.getJobSeeker().getRealName() : "求职者";
                String jobTitle   = interview.getJob()       != null ? interview.getJob().getTitle()           : "职位";

                Map<String, Object> notification = new HashMap<>();
                notification.put("id", 200000L + interview.getId());
                notification.put("type", "INTERVIEW");
                notification.put("title", "面试即将开始");
                notification.put("content", "面试安排：" + seekerName + " - " + jobTitle
                        + "，时间：" + interview.getInterviewTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                notification.put("isRead", "CONFIRMED".equals(interview.getStatus()) || "COMPLETED".equals(interview.getStatus()));
                notification.put("createTime", interview.getCreateTime());
                notification.put("relatedId", interview.getId());
                notification.put("relatedType", "INTERVIEW");
                notification.put("source", "USER");
                allNotifications.add(notification);
            }
        }

        // 3. 系统通知（来自用户端操作或管理端推送，存在 notification 表）
        if (userId != null) {
            List<Notification> sysNotifications = notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
            System.out.println("企业端通知查询: userId=" + userId + ", 找到系统通知数=" + sysNotifications.size());
            for (Notification n : sysNotifications) {
                Map<String, Object> notification = new HashMap<>();
                notification.put("id", 300000L + n.getId());
                notification.put("relatedId", n.getRelatedId());
                notification.put("relatedType", n.getRelatedType());
                notification.put("type", n.getType());
                notification.put("title", n.getTitle());
                notification.put("content", n.getContent());
                notification.put("isRead", n.getIsRead() == 1);
                notification.put("createTime", n.getCreateTime());
                notification.put("source", "ANNOUNCEMENT".equals(n.getType()) ? "ADMIN" : "SYSTEM");
                notification.put("rawId", n.getId());
                allNotifications.add(notification);
                System.out.println("添加系统通知: id=" + n.getId() + ", type=" + n.getType() + ", title=" + n.getTitle());
            }
        } else {
            System.out.println("企业端通知查询: userId 为 null，跳过系统通知查询");
        }

        // 4. 系统公告（直接从公告表读取，targetAudience 为 ALL 或 ENTERPRISE）
        List<Announcement> announcements = announcementRepository.findActiveAnnouncements(LocalDateTime.now(), "ENTERPRISE");
        for (Announcement a : announcements) {
            // 避免与 notification 表里的公告重复（notification 表已有 ANNOUNCEMENT 类型）
            boolean alreadyInNotifications = userId != null &&
                notificationRepository.findByUserIdOrderByCreateTimeDesc(userId)
                    .stream().anyMatch(n -> "ANNOUNCEMENT".equals(n.getType()) && a.getId().equals(n.getRelatedId()));
            if (alreadyInNotifications) continue;

            Map<String, Object> notification = new HashMap<>();
            notification.put("id", 400000L + a.getId());
            notification.put("relatedId", a.getId());
            notification.put("relatedType", "ANNOUNCEMENT");
            notification.put("type", "ANNOUNCEMENT");
            notification.put("title", "系统公告：" + a.getTitle());
            notification.put("content", a.getContent());
            notification.put("isRead", false);
            notification.put("createTime", a.getPublishTime());
            notification.put("source", "ADMIN");
            allNotifications.add(notification);
        }

        // 按时间倒序排列
        allNotifications.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("createTime");
            LocalDateTime timeB = (LocalDateTime) b.get("createTime");
            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;
            return timeB.compareTo(timeA);
        });

        int total = allNotifications.size();
        int start = page * size;
        int end   = Math.min(start + size, total);
        List<Map<String, Object>> pageContent = start < total ? allNotifications.subList(start, end) : new ArrayList<>();

        return new PageImpl<>(pageContent, PageRequest.of(page, size), total);
    }

    @Override
    public Long getUnreadCount(Long enterpriseId, Long userId) {
        long count = 0;

        // 申请未读
        List<Application> apps = applicationRepository.findByEnterpriseId(enterpriseId);
        count += apps.stream()
                .filter(a -> a.getViewTime() == null && !"NOTIF_READ".equals(a.getTag()))
                .count();

        // 系统通知未读
        if (userId != null) {
            count += notificationRepository.countUnreadByUserId(userId);
        }

        return count;
    }

    @Override
    @Transactional
    public void markAsRead(Long enterpriseId, Long userId, Long notificationId) {
        if (notificationId >= 100000L && notificationId < 200000L) {
            Long applicationId = notificationId - 100000L;
            Application app = applicationRepository.findById(applicationId).orElse(null);
            if (app != null && app.getEnterprise().getId().equals(enterpriseId)) {
                app.setTag("NOTIF_READ");
                applicationRepository.save(app);
            }
        } else if (notificationId >= 300000L && notificationId < 400000L) {
            Long rawId = notificationId - 300000L;
            notificationRepository.markAsRead(rawId);
        }
        // 400000+ 公告类暂不持久化已读状态
    }

    @Override
    @Transactional
    public void markAllAsRead(Long enterpriseId, Long userId) {
        // 标记申请通知已读
        List<Application> apps = applicationRepository.findByEnterpriseId(enterpriseId);
        for (Application app : apps) {
            app.setTag("NOTIF_READ");
        }
        applicationRepository.saveAll(apps);

        // 标记系统通知已读
        if (userId != null) {
            notificationRepository.markAllAsRead(userId);
        }
    }
}
