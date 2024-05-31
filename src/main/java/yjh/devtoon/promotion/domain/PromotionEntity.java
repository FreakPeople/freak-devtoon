package yjh.devtoon.promotion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "promotion")
@Entity
public class PromotionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_no", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    @Column(name = "discount_quantity")
    private Integer discountQuantity;

    @Column(name = "is_discount_duplicatable", nullable = false)
    private Boolean isDiscountDuplicatable;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public PromotionEntity(
            final Long id,
            final String description,
            final DiscountType discountType,
            final BigDecimal discountRate,
            final Integer discountQuantity,
            final Boolean isDiscountDuplicatable,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.description = description;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountQuantity = discountQuantity;
        this.isDiscountDuplicatable = isDiscountDuplicatable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deletedAt = deletedAt;
    }

    public static PromotionEntity create(
            final String description,
            final DiscountType discountType,
            final BigDecimal discountRate,
            final Integer discountQuantity,
            final Boolean isDiscountDuplicatable,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return PromotionEntity.builder()
                .description(description)
                .discountType(discountType)
                .discountRate(discountRate)
                .discountQuantity(discountQuantity)
                .isDiscountDuplicatable(isDiscountDuplicatable)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    /**
     * 프로모션 엔티티의 삭제 시간을 기록하는 메서드
     * : 실제 데이터베이스에서의 물리적 삭제는 이루어지지 않으며,
     * 삭제 시간을 통해 로직상에서 삭제 처리를 구분합니다.
     *
     * @param deletedAt 메서드가 호출되는 시점의 시간으로 삭제 처리를 기록
     */
    public void recordDeletion(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * 프로모션 할인 유형 중 '현금 할인'에 해당하는지 확인하는 메서드
     */
    public boolean isCashDiscount() {
        return discountType.equals(DiscountType.CASH_DISCOUNT);
    }

    /**
     * 중복 할인이 불가능함을 확인하는 메서드
     */
    public boolean isNotDiscountDuplicatable() {
        return !isDiscountDuplicatable;
    }

    /**
     * 프로모션 할인 유형 중 '웹툰 구매시 쿠키 개수 할인'에 해당하는지 확인하는 메서드
     */
    public boolean isCookieQuantityDiscountApplicable() {
        return discountType.equals(DiscountType.COOKIE_QUANTITY_DISCOUNT);
    }

    @Override
    public String toString() {
        return "PromotionEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", discountType=" + discountType +
                ", discountRate=" + discountRate +
                ", discountQuantity=" + discountQuantity +
                ", isDiscountDuplicatable=" + isDiscountDuplicatable +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", deletedAt=" + deletedAt +
                '}';
    }

    /**
     * 현재 또는 미래에 유효한 프로모션인지 확인하는 메서드
     */
    public boolean isCurrentOrFuture(final LocalDateTime now) {
        return endDate == null || now.isBefore(endDate) || now.isEqual(endDate);
    }

    /**
     * 현재 유효한 프로모션인지 확인하는 메서드
     */
    public boolean isCurrent(final LocalDateTime now) {
        return (now.isAfter(startDate) || now.isEqual(startDate)) &&
                (endDate == null || now.isBefore(endDate) || now.isEqual(endDate));
    }

}
