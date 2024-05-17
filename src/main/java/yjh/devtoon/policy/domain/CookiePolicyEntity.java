package yjh.devtoon.policy.domain;

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
import yjh.devtoon.policy.common.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "cookie_policy")
@Entity
public class CookiePolicyEntity extends BaseEntity implements Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cookie_policy_no", nullable = false)
    private Long id;

    @Column(name = "cookie_price", nullable = false)
    private BigDecimal cookiePrice;

    @Column(name = "cookie_quantity_per_episode", nullable = false)
    private Integer cookieQuantityPerEpisode;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Builder
    public CookiePolicyEntity(
            final Long id,
            final BigDecimal cookiePrice,
            final Integer cookieQuantityPerEpisode,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        this.id = id;
        this.cookiePrice = cookiePrice;
        this.cookieQuantityPerEpisode = cookieQuantityPerEpisode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static CookiePolicyEntity create(
            final BigDecimal cookiePrice,
            final Integer cookieQuantityPerEpisode,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return CookiePolicyEntity.builder()
                .cookiePrice(cookiePrice)
                .cookieQuantityPerEpisode(cookieQuantityPerEpisode)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public String toString() {
        return "CookiePolicyEntity{" +
                "id=" + id +
                ", cookiePrice=" + cookiePrice +
                ", cookieQuantityPerEpisode=" + cookieQuantityPerEpisode +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
