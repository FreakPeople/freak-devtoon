package yjh.devtoon.policy.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 모든 정책에 공통 사용 dto
 */
@NoArgsConstructor
@Getter
public class PolicyCreateRequest {

    private String policyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * 쿠키 정책
     */
    private BigDecimal cookiePrice;
    private Integer cookieQuantityPerEpisode;

    /**
     * 비속어 정책
     */
    private int warningThreshold;

    public PolicyCreateRequest(
            final String policyName,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final BigDecimal cookiePrice,
            final Integer cookieQuantityPerEpisode
    ) {
        this.policyName = policyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cookiePrice = cookiePrice;
        this.cookieQuantityPerEpisode = cookieQuantityPerEpisode;
    }

    public PolicyCreateRequest(
            final String policyName,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final int warningThreshold
    ) {
        this.policyName = policyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.warningThreshold = warningThreshold;
    }

}
