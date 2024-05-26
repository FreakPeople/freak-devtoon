package yjh.devtoon.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yjh.devtoon.promotion.domain.promotion.CookiePromotion;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<Long, CookiePromotion> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfter(new Expiry<Long, CookiePromotion>() {
                    @Override
                    public long expireAfterCreate(Long key, CookiePromotion value,
                                                  long currentTime) {
                        // 각 항목에 대해 개별적으로 만료 시간 지정 (예: 60분)
                        return TimeUnit.MINUTES.toNanos(60);
                    }

                    @Override
                    public long expireAfterUpdate(Long key, CookiePromotion value,
                                                  long currentTime, long currentDuration) {
                        // 업데이트 시 만료 시간 지정 (필요에 따라)
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(Long key, CookiePromotion value, long currentTime
                            , long currentDuration) {
                        // 읽기 시 만료 시간 지정 (필요에 따라)
                        return currentDuration;
                    }
                });
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
