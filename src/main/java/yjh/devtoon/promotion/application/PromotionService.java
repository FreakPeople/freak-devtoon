package yjh.devtoon.promotion.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;

@RequiredArgsConstructor
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;

    @Transactional
    public void register(final PromotionCreateRequest request) {

        PromotionEntity promotion = PromotionEntity.create(
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate()
        );

        PromotionEntity savedPromotionEntity = promotionRepository.save(promotion);

        PromotionAttributeEntity attribute = PromotionAttributeEntity.create(
                savedPromotionEntity,
                request.getAttributeName(),
                request.getAttributeValue()
        );

        promotionAttributeRepository.save(attribute);

    }

}
