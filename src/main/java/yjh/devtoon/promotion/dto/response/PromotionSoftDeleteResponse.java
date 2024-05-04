package yjh.devtoon.promotion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yjh.devtoon.promotion.domain.PromotionEntity;

@AllArgsConstructor
@Getter
public class PromotionSoftDeleteResponse {

    private final Long promotionId;

    public static PromotionSoftDeleteResponse from(final PromotionEntity promotionEntity) {
        return new PromotionSoftDeleteResponse(promotionEntity.getId());
    }

}
