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
import yjh.devtoon.promotion.constant.ErrorMessage;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;
import yjh.devtoon.promotion.dto.response.RetrieveActivePromotionsResponse;
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
     * 프로모션 소프트 삭제
     */
    @Transactional
    public PromotionSoftDeleteResponse softDelete(final Long id) {
        PromotionEntity promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound("Promotion", id)));

        promotion.recordDeletion(LocalDateTime.now());
        PromotionEntity softDeletedPromotion = promotionRepository.save(promotion);

        return PromotionSoftDeleteResponse.from(softDeletedPromotion);
    }

    /**
     * 현재 활성화된 프로모션 전체 조회
     * : 현재 활성화된 프로모션이 없는 경우 빈 페이지를 반환합니다.
     */
    @Transactional(readOnly = true)
    public Page<RetrieveActivePromotionsResponse> retrieveActivePromotions(
            final Pageable pageable
    ) {
        // 활성화된 프로모션 목록 조회
        Page<PromotionEntity> activePromotions = validateActivePromotionExists(pageable);

        // 각 프로모션 엔티티를 프로모션 속성과 함께 응답 객체로 변환
        return activePromotions.map(promotionEntity -> {
            PromotionAttributeEntity promotionAttribute =
                    validatePromotionAttributeExists(promotionEntity);
            return RetrieveActivePromotionsResponse.from(promotionEntity, promotionAttribute);
        });
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

    private PromotionAttributeEntity validatePromotionAttributeExists(
            final PromotionEntity promotionEntity
    ) {
        PromotionAttributeEntity promotionAttribute =
                promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId())
                        .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                                ErrorMessage.getResourceNotFound(
                                        "PromotionEntity",
                                        promotionEntity.getId()
                                ))
                        );
        return promotionAttribute;
    }

}
