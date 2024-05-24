package yjh.devtoon.promotion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;

@AllArgsConstructor
@Getter
public class RetrieveActivePromotionAttributesResponse {

    private final Long attributeId;
    private final Long promotionId;
    private final String attributeName;
    private final String attributeValue;

    public static RetrieveActivePromotionAttributesResponse from(
            final PromotionAttributeEntity promotionAttributeEntity
    ) {
        return new RetrieveActivePromotionAttributesResponse(
                promotionAttributeEntity.getId(),
                promotionAttributeEntity.getPromotionEntity().getId(),
                promotionAttributeEntity.getAttributeName(),
                promotionAttributeEntity.getAttributeValue()
        );
    }

}
