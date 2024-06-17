package yjh.devtoon.policy.dto.response;

import lombok.Getter;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import java.time.LocalDateTime;

@Getter
public class RetrieveBadWordsPolicyResponse {

    private int warningThreshold;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public RetrieveBadWordsPolicyResponse(
            int warningThreshold,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.warningThreshold = warningThreshold;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RetrieveBadWordsPolicyResponse from(
            final BadWordsPolicyEntity badWordsPolicyEntity
    ) {
        return new RetrieveBadWordsPolicyResponse(
                badWordsPolicyEntity.getWarningThreshold(),
                badWordsPolicyEntity.getStartDate(),
                badWordsPolicyEntity.getEndDate(),
                badWordsPolicyEntity.getCreatedAt(),
                badWordsPolicyEntity.getUpdatedAt()
        );
    }

}
