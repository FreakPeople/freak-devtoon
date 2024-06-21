package yjh.devtoon.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "cookie_payment")
@Entity
public class CookiePaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cookies_payment_no", nullable = false)
    private Long cookiesPaymentId;

    @Column(name = "member_no", nullable = false)
    private Long memberId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Embedded
    @Column(name = "cookie_price", nullable = false)
    private Price price;

    @Column(name = "total_discount_rate", nullable = false)
    private BigDecimal totalDiscountRate;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public CookiePaymentEntity(
            final Long cookiesPaymentId,
            final Long memberId,
            final Integer quantity,
            final Price price,
            final BigDecimal totalDiscountRate,
            final LocalDateTime deletedAt
    ) {
        this.cookiesPaymentId = cookiesPaymentId;
        this.memberId = memberId;
        this.quantity = quantity;
        this.price = price;
        this.totalDiscountRate = totalDiscountRate;
        this.deletedAt = deletedAt;
    }

    public static CookiePaymentEntity create(
            final Long memberId,
            final Integer quantity,
            final Price price,
            final BigDecimal totalDiscountRate
    ) {
        return CookiePaymentEntity.builder()
                .memberId(memberId)
                .quantity(quantity)
                .price(price)
                .totalDiscountRate(totalDiscountRate)
                .build();
    }

    public Price calculateTotalPrice() {
        return this.price.calculateTotalPrice(this.quantity);
    }

    public Price calculatePaymentPrice() {
        // 총 결제 가격 = 총 가격 * (1-totalDiscountRate)
        Price totalPrice = calculateTotalPrice();
        BigDecimal discountedPercentage = BigDecimal.ONE.subtract(this.totalDiscountRate);

        return totalPrice.multiply(Price.of(discountedPercentage));
    }

    @Override
    public String toString() {
        return "CookiePaymentEntity{" +
                "cookiesPaymentId=" + cookiesPaymentId +
                ", memberId=" + memberId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalDiscountRate=" + totalDiscountRate +
                ", deletedAt=" + deletedAt +
                '}';
    }

}
