package yjh.devtoon.promotion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "promotion_attribute")
@Entity
public class PromotionAttributeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_attribute_no", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_no")
    private PromotionEntity promotionEntity;

    @Column(name = "attribute_name", nullable = false)
    private String attributeName;

    @Column(name = "attribute_value", nullable = false)
    private String attributeValue;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public PromotionAttributeEntity(
            final Long id,
            final PromotionEntity promotionEntity,
            final String attributeName,
            final String attributeValue,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.promotionEntity = promotionEntity;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.deletedAt = deletedAt;
    }

    public static PromotionAttributeEntity create(
            final PromotionEntity promotionEntity,
            final String attributeName,
            final String attributeValue
    ) {
        return PromotionAttributeEntity.builder()
                .promotionEntity(promotionEntity)
                .attributeName(attributeName)
                .attributeValue(attributeValue)
                .build();
    }

    /**
     * 프로모션 할인 유형 중 '현금 할인'이 적용가능한지 확인하는 메서드
     */
    public boolean isCashDiscountApply(
            final WebtoonViewerEntity webtoonViewer,
            final int quantity
    ) {
        // TODO : 속성이 계속해서 추가될 수 있음. 리팩토링 해야함.
        if (attributeName.equals("premium_member_discount")) {
            return webtoonViewer.isPremium();
        } else if (attributeName.equals("cookie_purchase_quantity")) {
            return 10 < quantity;
        }
        return false;
    }

    /**
     * 프로모션 할인 유형 중 '웹툰 구매시 쿠키 개수 할인'이 적용가능한지 확인하는 메서드
     */
    public boolean isCookieQuantyDiscountApply(final WebtoonEntity webtoon) {
        if (attributeName.equals("target_genre")) {
            return webtoon.isGenre(attributeValue);
        }
        return false;
    }

    @Override
    public String toString() {
        return "PromotionAttributeEntity{" +
                "id=" + id +
                ", promotionEntity=" + promotionEntity +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }

}
