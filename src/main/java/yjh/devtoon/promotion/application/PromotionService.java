package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.promotion.constant.ErrorMessage;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionAttributeCreateRequest;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {

    private static final String CACHE_KEY = "promotion:active";

    private final PromotionRepository promotionRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;
    private final RedisTemplate<String, List<PromotionEntity>> promotionRedisTemplate;

    /**
     * 프로모션 등록
     */
    @Transactional
    public void register(final PromotionCreateRequest request) {
        PromotionEntity promotion = PromotionEntity.create(
                request.getDescription(),
                request.getDiscountType(),
                request.getDiscountRate(),
                request.getDiscountQuantity(),
                request.getIsDiscountDuplicatable(),
                request.getStartDate(),
                request.getEndDate()
        );
        PromotionEntity savedPromotion = promotionRepository.save(promotion);

        List<PromotionAttributeCreateRequest> promotionAttributeCreateRequests =
                request.getPromotionAttributes();

        promotionAttributeCreateRequests.stream()
                .map(promotionAttributeRequest -> toEntity(savedPromotion,
                        promotionAttributeRequest))
                .forEach(promotionAttributeRepository::save);

        cachePromotion(savedPromotion);
    }

    // 각 프로모션을 개별적으로 캐시에 저장하는 메서드
    private void cachePromotion(PromotionEntity promotion) {
        List<PromotionEntity> cachedPromotions =
                promotionRedisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedPromotions == null) {
            cachedPromotions = new ArrayList<>();
        }
        cachedPromotions.add(promotion);
        promotionRedisTemplate.opsForValue().set(CACHE_KEY, cachedPromotions);
    }

    private PromotionAttributeEntity toEntity(
            PromotionEntity savedPromotion,
            PromotionAttributeCreateRequest request
    ) {
        return PromotionAttributeEntity.create(
                savedPromotion,
                request.getAttributeName(),
                request.getAttributeValue());
    }

    /**
     * 프로모션 삭제(종료)
     * : 삭제 시간을 통해 로직상에서 삭제 처리를 구분합니다.
     */
    @Transactional
    public PromotionEntity delete(final Long id) {
        PromotionEntity promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.PROMOTION, id)
                ));
        promotion.recordDeletion(LocalDateTime.now());
        // 캐시에서도 삭제
        List<PromotionEntity> cachedPromotions =
                promotionRedisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedPromotions != null) {
            cachedPromotions.removeIf(p -> p.getId().equals(id));
            promotionRedisTemplate.opsForValue().set(CACHE_KEY, cachedPromotions);
        }
        return promotionRepository.save(promotion);
    }

    /**
     * 현재 적용 가능한 모든 프로모션 조회
     * : 프로모션만 조회합니다. 현재 적용 가능한 프로모션이 없는 경우 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    public List<PromotionEntity> retrieveActivePromotions() {
        List<PromotionEntity> promotions = getCachedPromotions();
        if (promotions.isEmpty()) {
            promotions = fetchAndCachePromotions();
        }
        return promotions;
    }

    /**
     * 캐시된 프로모션 조회
     * : 캐시된 프로모션이 없다면 빈 리스트를 반환합니다.
     */
    private List<PromotionEntity> getCachedPromotions() {
        List<PromotionEntity> cachedPromotions =
                promotionRedisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedPromotions != null && !cachedPromotions.isEmpty()) {
            return filterCurrentPromotions(cachedPromotions);
        }
        return Collections.emptyList();
    }

    /**
     * 프로모션을 DB에서 조회하여 캐시에 저장
     */
    private List<PromotionEntity> fetchAndCachePromotions() {
        List<PromotionEntity> promotions = retrieveCurrentOrFuturePromotion();
        // 모든 프로모션을 한 번에 캐시에 저장
        promotionRedisTemplate.opsForValue().set(CACHE_KEY, promotions);
        return filterCurrentPromotions(promotions);
    }

    private List<PromotionEntity> retrieveCurrentOrFuturePromotion() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PromotionEntity> promotions =
                promotionRepository.findCurrentOrFuturePromotions(currentTime);
        if (promotions.isEmpty()) {
            return Collections.emptyList();
        }
        return promotions;
    }

    private List<PromotionEntity> filterCurrentPromotions(final List<PromotionEntity> currentAndFuturePromotions) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentAndFuturePromotions.stream()
                .filter(p -> p.isCurrent(currentTime))
                .collect(Collectors.toList());
    }

    /**
     * 현재 적용 가능한 프로모션에 포함된 모든 프로모션 속성 조회
     * : 현재 적용 가능한 프로모션이 없는 경우 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    public List<PromotionAttributeEntity> retrieveActivePromotionAttributes(Long promotionId) {
        return findActivePromotionAttributes(promotionId);
    }

    private List<PromotionAttributeEntity> findActivePromotionAttributes(Long promotionId) {
        List<PromotionAttributeEntity> promotionAttributes =
                promotionAttributeRepository.findByPromotionEntityId(promotionId);
        if (promotionAttributes.isEmpty()) {
            return Collections.emptyList();
        }
        return promotionAttributes;
    }

    /**
     * 종료된 모든 프로모션 조회
     */
    @Transactional(readOnly = true)
    public List<PromotionEntity> retrieveAllEndedPromotions() {
        return promotionRepository.findEndedPromotions();
    }

}
