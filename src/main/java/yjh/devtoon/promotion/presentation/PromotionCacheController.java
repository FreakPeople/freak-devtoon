package yjh.devtoon.promotion.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/v1/promotions")
@RestController
public class PromotionCacheController {

    private final CacheManager cacheManager;

    /**
     * 임시 API
     * : 로컬 캐시 사용시 프로모션 등록, 조회 API 요청시 캐시에 데이터가 잘 들어갔는지 확인하는 메서드.
     */
    @GetMapping("/now/cache-details")
    public ResponseEntity<ApiResponse<List<Map<String, List<String>>>>> getPromotionCacheData() {
        List<Map<String, List<String>>> result = cacheManager.getCacheNames().stream()
                .map(this::getCacheDetails)
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private Map<String, List<String>> getCacheDetails(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (!(cache instanceof CaffeineCache)) {
            return null;
        }

        CaffeineCache caffeineCache = (CaffeineCache) cache;
        Map<Object, Object> nativeCache = caffeineCache.getNativeCache().asMap();
        Map<String, List<String>> entry = new HashMap<>();

        nativeCache.forEach((key, value) -> {
            if (value instanceof List) {
                List<?> valueList = (List<?>) value;
                List<String> promotions = new ArrayList<>();
                for (Object obj : valueList) {
                    if (obj instanceof PromotionEntity) {
                        promotions.add(obj.toString());
                    }
                }
                entry.computeIfAbsent(cacheName, k -> new ArrayList<>()).add(key.toString() + ": "
                        + promotions);
            } else {
                entry.computeIfAbsent(cacheName, k -> new ArrayList<>()).add(key.toString() + ": "
                        + value.toString());
            }
        });
        return entry;
    }

}
