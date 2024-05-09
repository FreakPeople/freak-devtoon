package yjh.devtoon.payment.domain;

import jakarta.persistence.Column;
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
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "webtoon_payment")
@Entity
public class WebtoonPaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_payment_no", nullable = false)
    private Long webtoonPaymentId;

    @Column(name = "webtoon_viewer_no", nullable = false)
    private Long webtoonViewerId;

    @Column(name = "webtoon_no", nullable = false)
    private Long webtoonId;

    @Column(name = "webtoon_detail_no", nullable = false)
    private Long webtoonDetailId;

    @Column(name = "cookie_payment_amount", nullable = false)
    private Long cookiePaymentAmount;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public WebtoonPaymentEntity(
            final Long webtoonPaymentId,
            final Long webtoonViewerId,
            final Long webtoonId,
            final Long webtoonDetailId,
            final Long cookiePaymentAmount,
            final LocalDateTime deletedAt
    ) {
        this.webtoonPaymentId = webtoonPaymentId;
        this.webtoonViewerId = webtoonViewerId;
        this.webtoonId = webtoonId;
        this.webtoonDetailId = webtoonDetailId;
        this.cookiePaymentAmount = cookiePaymentAmount;
        this.deletedAt = deletedAt;
    }

    public static WebtoonPaymentEntity create(
            final Long webtoonViewerId,
            final Long webtoonId,
            final Long webtoonDetailId,
            final Long cookiePaymentAmount
    ) {
        return WebtoonPaymentEntity.builder()
                .webtoonViewerId(webtoonViewerId)
                .webtoonId(webtoonId)
                .webtoonDetailId(webtoonDetailId)
                .cookiePaymentAmount(cookiePaymentAmount)
                .build();
    }

}
