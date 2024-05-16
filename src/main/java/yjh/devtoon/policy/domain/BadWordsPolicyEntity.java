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
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "bad_words_policy")
@Entity
public class BadWordsPolicyEntity extends BaseEntity implements Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bad_words_policy_no", nullable = false)
    private Long id;

    @Column(name = "warning_threshold", nullable = false)
    private int warningThreshold;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Builder
    public BadWordsPolicyEntity(
            final int warningThreshold,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        this.warningThreshold = warningThreshold;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static BadWordsPolicyEntity create(
            final int warningThreshold,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return new BadWordsPolicyEntity().builder()
                .warningThreshold(warningThreshold)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public String toString() {
        return "BadWordsPolicyEntity{" +
                "id=" + id +
                ", warningThreshold=" + warningThreshold +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
