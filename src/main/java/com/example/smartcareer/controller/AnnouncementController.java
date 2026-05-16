package com.example.smartcareer.controller;

import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.entity.Announcement;
import com.example.smartcareer.service.AnnouncementService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/admin/create")
    public ApiResponse<Announcement> createAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) {
        Long adminId = getCurrentUserId(request);
        String username = getCurrentUsername(request);
        announcement.setAdminId(adminId);
        announcement.setAdminName(username);
        Announcement created = announcementService.createAnnouncement(announcement);
        return ApiResponse.success(created);
    }
    
    @PutMapping("/admin/{id}")
    public ApiResponse<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        Announcement updated = announcementService.updateAnnouncement(id, announcement);
        return ApiResponse.success(updated);
    }
    
    @DeleteMapping("/admin/{id}")
    public ApiResponse<String> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ApiResponse.success("公告已删除");
    }
    
    @PostMapping("/admin/{id}/publish")
    public ApiResponse<Announcement> publishAnnouncement(@PathVariable Long id, HttpServletRequest request) {
        Long adminId = getCurrentUserId(request);
        Announcement published = announcementService.publishAnnouncement(id, adminId);
        return ApiResponse.success(published);
    }
    
    @PostMapping("/admin/{id}/unpublish")
    public ApiResponse<Announcement> unpublishAnnouncement(@PathVariable Long id) {
        Announcement unpublished = announcementService.unpublishAnnouncement(id);
        return ApiResponse.success(unpublished);
    }
    
    @GetMapping("/admin/list")
    public ApiResponse<Page<Announcement>> getAnnouncementList(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer isPublished,
            @RequestParam(required = false) String targetAudience,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Announcement> announcements = announcementService.getAnnouncementList(type, isPublished, targetAudience, page, size);
        return ApiResponse.success(announcements);
    }
    
    @GetMapping("/admin/statistics")
    public ApiResponse<Map<String, Object>> getAnnouncementStatistics() {
        Map<String, Object> stats = announcementService.getAnnouncementStatistics();
        return ApiResponse.success(stats);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Announcement> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        announcementService.incrementViewCount(id);
        return ApiResponse.success(announcement);
    }
    
    @GetMapping("/active")
    public ApiResponse<List<Announcement>> getActiveAnnouncements(HttpServletRequest request) {
        String audience = getUserAudience(request);
        List<Announcement> announcements = announcementService.getActiveAnnouncements(audience);
        return ApiResponse.success(announcements);
    }
    
    @GetMapping("/active/page")
    public ApiResponse<Page<Announcement>> getActiveAnnouncementsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        String audience = getUserAudience(request);
        Page<Announcement> announcements = announcementService.getActiveAnnouncements(audience, page, size);
        return ApiResponse.success(announcements);
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
    
    private String getCurrentUsername(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUsername(token);
    }
    
    private String getUserAudience(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String userType = jwtUtil.extractUserType(token);
                if ("JOB_SEEKER".equals(userType)) {
                    return "JOB_SEEKER";
                } else if ("ENTERPRISE".equals(userType) || "ENTERPRISE_HR".equals(userType) || "COMPANY".equals(userType)) {
                    return "ENTERPRISE";
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return "ALL";
    }
}