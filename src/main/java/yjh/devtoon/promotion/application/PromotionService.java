package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.promotion.constant.ErrorMessage;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;

    /**
     * 프로모션 등록
     */
    @Transactional
    public void register(final PromotionCreateRequest request) {
        PromotionEntity promotion = PromotionEntity.create(
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate()
        );
        PromotionEntity savedPromotion = promotionRepository.save(promotion);

        PromotionAttributeEntity attribute = PromotionAttributeEntity.create(
                savedPromotion,
                request.getAttributeName(),
                request.getAttributeValue()
        );
        promotionAttributeRepository.save(attribute);
    }

    /**
     * 프로모션 삭제
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
     * 현재 활성화된 프로모션 전체 조회
     * : 현재 활성화된 프로모션이 없는 경우 빈 페이지를 반환합니다.
     */
    @Transactional(readOnly = true)
    public Page<PromotionEntity> retrieveActivePromotions(final Pageable pageable) {
        Page<PromotionEntity> activePromotions = validateActivePromotionExists(pageable);
        return activePromotions;
    }

    private Page<PromotionEntity> validateActivePromotionExists(final Pageable pageable) {
        LocalDateTime currentTime = LocalDateTime.now();
        Page<PromotionEntity> promotions = promotionRepository.findActivePromotions(
                currentTime, pageable
        );

        if (promotions.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        return promotions;
    }

    public PromotionAttributeEntity validatePromotionAttributeExists(
            final PromotionEntity promotionEntity
    ) {
        PromotionAttributeEntity promotionAttribute =
                promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId())
                        .orElseThrow(() -> new DevtoonException(
                                ErrorCode.NOT_FOUND,
                                ErrorMessage.getResourceNotFound(
                                        ResourceType.PROMOTION,
                                        promotionEntity.getId()
                                ))
                        );
        return promotionAttribute;
    }

}
