package yjh.devtoon.promotion.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import yjh.devtoon.promotion.domain.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PromotionCreateRequest {

    @NotBlank(message = "description을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String description;

    @NotNull(message = "할인 유형은 기재는 필수입니다.")
    private final DiscountType discountType;

    private final BigDecimal discountRate;

    private final Integer discountQuantity;

    @NotNull(message = "중복 할인 가능 여부 확인은 필수입니다.")
    private final Boolean isDiscountDuplicatable;

    @NotNull(message = "시작 날짜는 필수입니다.")
    @FutureOrPresent(message = "시작 날짜는 현재 시간이나 그 이후여야 합니다.")
    private final LocalDateTime startDate;

    @FutureOrPresent(message = "종료 날짜는 현재 시간이나 그 이후여야 합니다.")
    private final LocalDateTime endDate;

    @NotNull
    @Valid
    private final List<PromotionAttributeCreateRequest> promotionAttributes;

    public PromotionCreateRequest(
            final String description,
            final DiscountType discountType,
            final BigDecimal discountRate,
            final Integer discountQuantity,
            final Boolean isDiscountDuplicatable,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final List<PromotionAttributeCreateRequest> promotionAttributes
    ) {
        this.description = description;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountQuantity = discountQuantity;
        this.isDiscountDuplicatable = isDiscountDuplicatable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionAttributes = promotionAttributes;
    }

    @Override
    public String toString() {
        return "PromotionCreateRequest{" +
                "description='" + description + '\'' +
                ", discountType=" + discountType +
                ", discountRate=" + discountRate +
                ", discountQuantity=" + discountQuantity +
                ", isDiscountDuplicatable=" + isDiscountDuplicatable +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", promotionAttributes=" + promotionAttributes +
                '}';
    }
}
