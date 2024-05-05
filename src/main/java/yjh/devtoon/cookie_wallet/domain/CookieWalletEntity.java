package yjh.devtoon.cookie_wallet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "cookie_wallet")
public class CookieWalletEntity extends BaseEntity {

    @Id
    @Column(name = "webtoon_viewer_no", nullable = false)
    private Long webtoonViewerId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public CookieWalletEntity(
            final Long webtoonViewerId,
            final Integer quantity,
            final LocalDateTime deletedAt
    ) {
        this.webtoonViewerId = webtoonViewerId;
        this.quantity = quantity;
        this.deletedAt = deletedAt;
    }

    public static CookieWalletEntity create(final WebtoonViewerEntity webtoonViewerEntity) {
        return CookieWalletEntity.builder()
                .webtoonViewerId(webtoonViewerEntity.getId())
                .quantity(0)
                .build();
    }

    public void increase(final int quantity) {
        this.quantity += quantity;
    }

    public void decrease(final int quantity) {
        this.quantity -= quantity;
    }

}