package yjh.devtoon.promotion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yjh.devtoon.promotion.domain.DiscountType;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class RetrieveActivePromotionsResponse {

    private final Long promotionId;
    private final String description;
    private final DiscountType discountType;
    private final BigDecimal discountRate;
    private final Integer discountQuantity;
    private final Boolean isDiscountDuplicatable;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public static RetrieveActivePromotionsResponse from(
            final PromotionEntity promotionEntity
    ) {
        return new RetrieveActivePromotionsResponse(
                promotionEntity.getId(),
                promotionEntity.getDescription(),
                promotionEntity.getDiscountType(),
                promotionEntity.getDiscountRate(),
                promotionEntity.getDiscountQuantity(),
                promotionEntity.getIsDiscountDuplicatable(),
                promotionEntity.getStartDate(),
                promotionEntity.getEndDate()
        );
    }

}
