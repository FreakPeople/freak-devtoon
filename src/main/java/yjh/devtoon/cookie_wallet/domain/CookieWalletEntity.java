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
import yjh.devtoon.member.domain.MemberEntity;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "cookie_wallet")
public class CookieWalletEntity extends BaseEntity {

    @Id
    @Column(name = "member_no", nullable = false)
    private Long memberId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public CookieWalletEntity(
            final Long memberId,
            final Integer quantity,
            final LocalDateTime deletedAt
    ) {
        this.memberId = memberId;
        this.quantity = quantity;
        this.deletedAt = deletedAt;
    }

    public static CookieWalletEntity create(final MemberEntity memberEntity) {
        return CookieWalletEntity.builder()
                .memberId(memberEntity.getId())
                .quantity(0)
                .build();
    }

    public void increase(final int quantity) {
        this.quantity += quantity;
    }

    public void decrease(final int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        return "CookieWalletEntity{" +
                "memberId=" + memberId +
                ", quantity=" + quantity +
                ", deletedAt=" + deletedAt +
                '}';
    }

}