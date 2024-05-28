package yjh.devtoon.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(this::buildCache)
                .toList();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    private CaffeineCache buildCache(CacheType cacheType) {
        return new CaffeineCache(
                cacheType.getName(),
                Caffeine.newBuilder()
                        .expireAfterWrite(cacheType.getExpireAfterWrite(), TimeUnit.SECONDS)
                        .maximumSize(cacheType.getMaximumSize())
                        .build()
        );
    }

}
