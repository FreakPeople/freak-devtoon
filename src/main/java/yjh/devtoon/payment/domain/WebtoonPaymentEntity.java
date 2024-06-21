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

    @Column(name = "member_no", nullable = false)
    private Long memberId;

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
            final Long memberId,
            final Long webtoonId,
            final Long webtoonDetailId,
            final Long cookiePaymentAmount,
            final LocalDateTime deletedAt
    ) {
        this.webtoonPaymentId = webtoonPaymentId;
        this.memberId = memberId;
        this.webtoonId = webtoonId;
        this.webtoonDetailId = webtoonDetailId;
        this.cookiePaymentAmount = cookiePaymentAmount;
        this.deletedAt = deletedAt;
    }

    public static WebtoonPaymentEntity create(
            final Long memberId,
            final Long webtoonId,
            final Long webtoonDetailId,
            final Long cookiePaymentAmount
    ) {
        return WebtoonPaymentEntity.builder()
                .memberId(memberId)
                .webtoonId(webtoonId)
                .webtoonDetailId(webtoonDetailId)
                .cookiePaymentAmount(cookiePaymentAmount)
                .build();
    }

    @Override
    public String toString() {
        return "WebtoonPaymentEntity{" +
                "webtoonPaymentId=" + webtoonPaymentId +
                ", memberId=" + memberId +
                ", webtoonId=" + webtoonId +
                ", webtoonDetailId=" + webtoonDetailId +
                ", cookiePaymentAmount=" + cookiePaymentAmount +
                ", deletedAt=" + deletedAt +
                '}';
    }

}
