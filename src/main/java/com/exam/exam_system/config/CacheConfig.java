package com.exam.exam_system.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Per-cache TTL configuration separated by data volatility:
 *
 * STATIC  (30 min) — colleges, departments, subjects, roles, permissions
 *   Rarely change in production. allEntries=true eviction is acceptable.
 *
 * MEDIUM  (10 min) — users, students, exams
 *   Change occasionally. Key-based eviction keeps hit rate high.
 *
 * SHORT   (2 min)  — userDetails (auth filter), studentStats (dashboard)
 *   Called on every request / dashboard load. Must stay fresh.
 *
 * REMOVED — examSessions, examAccess, choices, questions, results, studentsanswers
 *   Time-sensitive / security-critical / never referenced. Must NOT be cached.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(
                // ── STATIC data: long TTL, rarely mutated ──
                buildCache("colleges",     30, TimeUnit.MINUTES, 500),
                buildCache("departments",  30, TimeUnit.MINUTES, 1_000),
                buildCache("subjects",     30, TimeUnit.MINUTES, 2_000),
                buildCache("roles",        30, TimeUnit.MINUTES, 100),
                buildCache("permissions",  30, TimeUnit.MINUTES, 500),

                // ── MEDIUM volatility: normal TTL ──
                buildCache("users",    10, TimeUnit.MINUTES, 5_000),
                buildCache("students", 10, TimeUnit.MINUTES, 5_000),
                buildCache("exams",    10, TimeUnit.MINUTES, 2_000),

                // ── SHORT TTL: high-frequency lookups that must stay fresh ──
                buildCache("userDetails",  2, TimeUnit.MINUTES, 1_000),
                buildCache("studentStats", 2, TimeUnit.MINUTES, 50)
        ));
        return manager;
    }

    private CaffeineCache buildCache(String name, long duration, TimeUnit unit, long maxSize) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .expireAfterWrite(duration, unit)
                        .maximumSize(maxSize)
                        .recordStats()
                        .build());
    }
}