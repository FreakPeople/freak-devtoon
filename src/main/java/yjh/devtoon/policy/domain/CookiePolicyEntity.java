package yjh.devtoon.policy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;
import yjh.devtoon.policy.common.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

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
    @Min(0)
    private BigDecimal cookiePrice;

    @Column(name = "cookie_quantity_per_episode", nullable = false)
    @Min(0)
    private Integer cookieQuantityPerEpisode;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * 요청 DTO에서 받은 세부 정보를 기반으로 객체의 상태를 초기화합니다.
     */
    public CookiePolicyEntity(Map<String, Object> details) {
        this.cookiePrice = new BigDecimal(details.get("cookiePrice").toString());
        this.cookieQuantityPerEpisode = Integer.parseInt(details.get("cookieQuantityPerEpisode").toString());
        this.startDate = LocalDateTime.parse(details.get("startDate").toString());
        this.endDate = details.containsKey("endDate") ? LocalDateTime.parse(details.get("endDate").toString()) : null;
    }

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

    /**
     * 정책 적용 메서드는 시스템에서 정책을 적용할 때 호출됩니다.
     */
    @Override
    public void applyPolicy() {
        System.out.println("쿠키 정책이 적용되었습니다. : ID = " + id + ", 가격 = " + cookiePrice + ", 수량 = " + cookieQuantityPerEpisode);
    }

    @Override
    public String toString() {
        return String.format("CookiePolicy {id=%d, price=%s, quantity=%d, start=%s, end=%s}",
                id, cookiePrice, cookieQuantityPerEpisode, startDate, endDate);
    }

}
