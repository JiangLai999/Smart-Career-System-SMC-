package com.example.smartcareer.config;

import com.example.smartcareer.repository.SystemLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class LogCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(LogCleanupScheduler.class);
    
    private static final int LOG_RETENTION_DAYS = 90;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldLogs() {
        logger.info("开始清理过期系统日志...");
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(LOG_RETENTION_DAYS);
        try {
            int deletedCount = systemLogRepository.deleteOldLogs(cutoffTime);
            logger.info("系统日志清理完成，删除了 {} 条记录", deletedCount);
        } catch (Exception e) {
            logger.error("系统日志清理失败", e);
        }
    }
}
