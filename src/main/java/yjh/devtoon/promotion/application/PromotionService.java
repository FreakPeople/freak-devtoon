package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.promotion.constant.ErrorMessage;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;

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

}
