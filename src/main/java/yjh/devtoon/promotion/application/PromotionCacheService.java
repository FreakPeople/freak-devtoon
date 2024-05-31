package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionCacheService {

    private final PromotionRepository promotionRepository;

    /**
     * @CachePut: 기존 캐시 항목은 그대로 유지, 새로 등록된 항목만 캐시에 추가
     */
    @CachePut(value = "promotion", key = "#promotion.id")
    public PromotionEntity updatePromotionInCache(PromotionEntity promotion) {
        log.info("프로모션 데이터를 캐시에 추가합니다: {}", promotion.getId());
        return promotion;
    }

    @CacheEvict(value = "promotion", key = "#promotionId")
    public void evictPromotionFromCache(Long promotionId) {
        log.info("캐시에서 특정 프로모션 데이터를 삭제합니다: {}", promotionId);
    }

}
