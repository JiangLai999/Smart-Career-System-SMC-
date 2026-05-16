package com.example.smartcareer.constant;

public final class JobStatus {
    
    private JobStatus() {}
    
    public static final String DRAFT = "DRAFT";
    public static final String PUBLISHED = "PUBLISHED";
    public static final String ACTIVE = "ACTIVE";
    public static final String CLOSED = "CLOSED";
    public static final String PAUSED = "PAUSED";
    public static final String EXPIRED = "EXPIRED";
    public static final String DELETED = "DELETED";
    
    public static boolean isActive(String status) {
        return PUBLISHED.equals(status) || ACTIVE.equals(status);
    }
    
    public static boolean isInactive(String status) {
        return CLOSED.equals(status) || PAUSED.equals(status) || EXPIRED.equals(status) || DELETED.equals(status);
    }
    
    public static String getDisplayText(String status) {
        switch (status) {
            case DRAFT: return "草稿";
            case PUBLISHED:
            case ACTIVE: return "在线";
            case CLOSED: return "已下线";
            case PAUSED: return "已暂停";
            case EXPIRED: return "已过期";
            case DELETED: return "已删除";
            default: return status;
        }
    }
}
