package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.promotion.constant.ErrorMessage;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.request.RetrieveActivePromotionsRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;
import yjh.devtoon.promotion.dto.response.RetrieveActivePromotionsResponse;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;
import java.util.Optional;

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
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("Promotion", id)));

        promotion.recordDeletion(LocalDateTime.now());
        PromotionEntity softDeletedPromotion = promotionRepository.save(promotion);

        return PromotionSoftDeleteResponse.from(softDeletedPromotion);

    }

    /**
     * 현재 적용 가능한 프로모션 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<RetrieveActivePromotionsResponse> retrieveActivePromotions(
            final RetrieveActivePromotionsRequest request,
            Pageable pageable
    ) {
        // 유효한 프로모션의 존재 여부를 확인
        if (!promotionRepository.existsByDateRange(request.getStartDate(), request.getEndDate())) {
            throw new DevtoonException(
                    ErrorCode.NOT_FOUND,
                    ErrorMessage.getResourceNotFound(
                            "Promotion",
                            String.format("from %s to %s", request.getStartDate(), request.getEndDate())
                    )
            );
        }

        // 유효한 프로모션 조회
        Page<PromotionEntity> activePromotions = promotionRepository.findActivePromotions(request.getStartDate(), request.getEndDate(), pageable);
        log.info("조회된 유효 프로모션 수: {}", activePromotions.getNumberOfElements());

        // 프로모션 엔티티를 응답 DTO로 변환
        return activePromotions.map(promotionEntity -> {
            Optional<PromotionAttributeEntity> promotionAttributeEntity = promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId());

            PromotionAttributeEntity attributeEntity = promotionAttributeEntity.orElseThrow(() ->
                    new DevtoonException(
                            ErrorCode.NOT_FOUND,
                            String.format("Attribute for promotion ID %s not found.", promotionEntity.getId())
                    )
            );
            log.info("성공적으로 조회된 attribute: {} for promotion: {}", attributeEntity, promotionEntity);

            return RetrieveActivePromotionsResponse.from(promotionEntity, attributeEntity);
        });
    }

}
