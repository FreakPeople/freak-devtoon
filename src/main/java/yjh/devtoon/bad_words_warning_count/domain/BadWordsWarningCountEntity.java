package yjh.devtoon.bad_words_warning_count.domain;

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
@Table(name = "bad_words_warning_count")
public class BadWordsWarningCountEntity extends BaseEntity {

    @Id
    @Column(name = "member_no", nullable = false)
    private Long memberId;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public BadWordsWarningCountEntity(
            final Long memberId,
            final Integer count,
            final LocalDateTime deletedAt
    ) {
        this.memberId = memberId;
        this.count = count;
        this.deletedAt = deletedAt;
    }

    public static BadWordsWarningCountEntity create(final MemberEntity memberEntity) {
        return BadWordsWarningCountEntity.builder()
                .memberId(memberEntity.getId())
                .count(0)
                .build();
    }

    public BadWordsWarningCountEntity increase() {
        this.count += 1;
        return this;
    }

    @Override
    public String toString() {
        return "BadWordsWarningCountEntity{" +
                "memberId=" + memberId +
                ", count=" + count +
                ", deletedAt=" + deletedAt +
                '}';
    }

}
