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

    /**
     * 프로모션 객체 생성 메서드
     * : 주어진 설명, 시작 날짜, 종료 날짜를 사용하여 새로운 PromotionEntity 인스턴스를 생성합니다.
     *
     * @param description 프로모션에 대한 설명
     * @param startDate   프로모션의 시작 날짜 및 시간
     * @param endDate     프로모션의 종료 날짜 및 시간
     * @return 새로 생성된 PromotionEntity 객체
     */
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

    public boolean isCashDiscount() {
        return discountType.equals(DiscountType.CASH_DISCOUNT);
    }


    public boolean isNotDiscountDuplicatable() {
        return !isDiscountDuplicatable;
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

}
