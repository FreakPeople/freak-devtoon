package yjh.devtoon.promotion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class RetrieveActivePromotionsResponse {

    private final Long promotionId;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String attributeName;
    private final String attributeValue;

    public static RetrieveActivePromotionsResponse from(
            final PromotionEntity promotionEntity,
            final PromotionAttributeEntity promotionAttributeEntity
    ) {
        return new RetrieveActivePromotionsResponse(
                promotionEntity.getId(),
                promotionEntity.getDescription(),
                promotionEntity.getStartDate(),
                promotionEntity.getEndDate(),
                promotionAttributeEntity.getAttributeName(),
                promotionAttributeEntity.getAttributeValue()
        );
    }

    @Override
    public String toString() {
        return "PromotionListResponse{" +
                "promotionId=" + promotionId +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }

}
