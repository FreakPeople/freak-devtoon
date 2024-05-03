package yjh.devtoon.promotion.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "promotion")
@Entity
public class PromotionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_no", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public PromotionEntity(
            final Long id,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deletedAt = deletedAt;
    }

    public static PromotionEntity create(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return PromotionEntity.builder()
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
