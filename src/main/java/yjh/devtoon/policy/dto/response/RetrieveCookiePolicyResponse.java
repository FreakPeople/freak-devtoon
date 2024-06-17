package yjh.devtoon.policy.dto.response;

import lombok.Getter;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class RetrieveCookiePolicyResponse {

    private BigDecimal cookiePrice;
    private Integer cookieQuantityPerEpisode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public RetrieveCookiePolicyResponse(
            BigDecimal cookiePrice,
            Integer cookieQuantityPerEpisode,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.cookiePrice = cookiePrice;
        this.cookieQuantityPerEpisode = cookieQuantityPerEpisode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RetrieveCookiePolicyResponse from(
            final CookiePolicyEntity cookiePolicyEntity
    ) {
        return new RetrieveCookiePolicyResponse(
                cookiePolicyEntity.getCookiePrice(),
                cookiePolicyEntity.getCookieQuantityPerEpisode(),
                cookiePolicyEntity.getStartDate(),
                cookiePolicyEntity.getEndDate(),
                cookiePolicyEntity.getCreatedAt(),
                cookiePolicyEntity.getUpdatedAt()
        );
    }

}
