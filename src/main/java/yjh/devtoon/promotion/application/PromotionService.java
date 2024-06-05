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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;
    private final RedisTemplate<String, List<PromotionEntity>> promotionRedisTemplate;
    private static final String CACHE_KEY = "promotion:active:list";
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

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
     * [관리자용] 프로모션 삭제
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
        return promotionRepository.save(promotion);
    }

    /**
     * 현재 적용 가능한 모든 프로모션 조회
     * : 프로모션만 조회합니다. 프로모션이 없는 경우 빈 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    public List<PromotionEntity> retrieveActivePromotions() {
        List<PromotionEntity> promotions = getCachedPromotions();
        if (promotions == null) {
            promotions = fetchAndCachePromotions();
        }
        return promotions;
    }

    /**
     * 캐시에서 프로모션 조회
     */
    private List<PromotionEntity> getCachedPromotions() {
        List<PromotionEntity> cachedPromotions =
                promotionRedisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedPromotions != null) {
            return filterCurrentPromotions(cachedPromotions);
        }
        return null;
    }

    /**
     * DB에서 프로모션 조회 후 캐시에 저장
     */
    private List<PromotionEntity> fetchAndCachePromotions() {
        List<PromotionEntity> promotions = retrieveCurrentOrFuturePromotion();
        promotionRedisTemplate.opsForValue().set(CACHE_KEY, promotions, CACHE_DURATION);
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
                .toList();
    }

    /**
     * 현재 적용 가능한 프로모션에 포함된 모든 프로모션 속성 조회
     */
    @Transactional(readOnly = true)
    public List<PromotionAttributeEntity> retrieveActivePromotionAttributes(Long promotionId) {
        List<PromotionAttributeEntity> activePromotionAttributes =
                findActivePromotionAttributes(promotionId);
        return activePromotionAttributes;
    }

    private List<PromotionAttributeEntity> findActivePromotionAttributes(Long promotionId) {
        List<PromotionAttributeEntity> promotionAttributes =
                promotionAttributeRepository.findByPromotionEntityId(promotionId);

        if (promotionAttributes.isEmpty()) {
            return Collections.emptyList();
        }
        return promotionAttributes;
    }

}
