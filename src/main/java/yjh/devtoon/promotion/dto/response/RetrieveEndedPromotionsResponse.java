package yjh.devtoon.promotion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.promotion.domain.DiscountType;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RetrieveEndedPromotionsResponse {

    private Long promotionId;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountRate;
    private Integer discountQuantity;
    private Boolean isDiscountDuplicatable;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    protected LocalDateTime deletedAt;

    public static RetrieveEndedPromotionsResponse from(
            final PromotionEntity promotionEntity
    ) {
        return new RetrieveEndedPromotionsResponse(
                promotionEntity.getId(),
                promotionEntity.getDescription(),
                promotionEntity.getDiscountType(),
                promotionEntity.getDiscountRate(),
                promotionEntity.getDiscountQuantity(),
                promotionEntity.getIsDiscountDuplicatable(),
                promotionEntity.getStartDate(),
                promotionEntity.getEndDate(),
                promotionEntity.getDeletedAt()
        );
    }

}
