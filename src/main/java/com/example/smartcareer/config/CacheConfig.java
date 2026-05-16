package com.example.smartcareer.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Redis相关导入（需要添加Redis依赖后启用）
// import org.springframework.data.redis.cache.RedisCacheConfiguration;
// import org.springframework.data.redis.cache.RedisCacheManager;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializationContext;
// import org.springframework.data.redis.serializer.StringRedisSerializer;

// import java.time.Duration;
// import java.util.HashMap;
// import java.util.Map;

/**
 * 缓存配置类
 * 用于提升系统性能，减少数据库查询
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    /**
     * 缓存名称常量
     */
    public static final String CACHE_USER = "user";
    public static final String CACHE_JOB = "job";
    public static final String CACHE_ENTERPRISE = "enterprise";
    public static final String CACHE_APPLICATION = "application";
    public static final String CACHE_ASSESSMENT = "assessment";
    public static final String CACHE_SETTINGS = "settings";
    public static final String CACHE_STATISTICS = "statistics";
    
    /**
     * 默认缓存管理器 (使用内存缓存，适用于开发环境)
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
            CACHE_USER,
            CACHE_JOB,
            CACHE_ENTERPRISE,
            CACHE_APPLICATION,
            CACHE_ASSESSMENT,
            CACHE_SETTINGS,
            CACHE_STATISTICS
        );
    }
    
    /**
     * Redis缓存管理器 (适用于生产环境)
     * 使用Redis实现分布式缓存
     */
    // @Bean
    // public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
    //     // 默认缓存配置
    //     RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
    //         .entryTtl(Duration.ofMinutes(30)) // 默认30分钟过期
    //         .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    //         .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
    //         .disableCachingNullValues();
    //     
    //     // 针对不同缓存名称的个性化配置
    //     Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    //     
    //     // 用户信息缓存 - 1小时
    //     cacheConfigurations.put(CACHE_USER, defaultConfig.entryTtl(Duration.ofHours(1)));
    //     
    //     // 职位信息缓存 - 5分钟
    //     cacheConfigurations.put(CACHE_JOB, defaultConfig.entryTtl(Duration.ofMinutes(5)));
    //     
    //     // 企业信息缓存 - 30分钟
    //     cacheConfigurations.put(CACHE_ENTERPRISE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
    //     
    //     // 申请信息缓存 - 10分钟
    //     cacheConfigurations.put(CACHE_APPLICATION, defaultConfig.entryTtl(Duration.ofMinutes(10)));
    //     
    //     // 测评信息缓存 - 1小时
    //     cacheConfigurations.put(CACHE_ASSESSMENT, defaultConfig.entryTtl(Duration.ofHours(1)));
    //     
    //     // 系统设置缓存 - 1天
    //     cacheConfigurations.put(CACHE_SETTINGS, defaultConfig.entryTtl(Duration.ofDays(1)));
    //     
    //     // 统计数据缓存 - 5分钟
    //     cacheConfigurations.put(CACHE_STATISTICS, defaultConfig.entryTtl(Duration.ofMinutes(5)));
    //     
    //     return RedisCacheManager.builder(connectionFactory)
    //         .cacheDefaults(defaultConfig)
    //         .withInitialCacheConfigurations(cacheConfigurations)
    //         .transactionAware()
    //         .build();
    // }
}
