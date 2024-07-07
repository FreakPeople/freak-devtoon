package yjh.devtoon.comment.domain;

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
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no", nullable = false)
    private Long id;

    @Column(name = "webtoon_no", nullable = false)
    private Long webtoonId;

    @Column(name = "member_no", nullable = false)
    private Long memberId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public CommentEntity(
            final Long id,
            final Long webtoonId,
            final Long memberId,
            final String content,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.webtoonId = webtoonId;
        this.memberId = memberId;
        this.content = content;
        this.deletedAt = deletedAt;
    }

    public static CommentEntity create(
            final Long webtoonId,
            final Long writerId,
            final String content
    ) {
        return CommentEntity.builder()
                .webtoonId(webtoonId)
                .memberId(writerId)
                .content(content)
                .build();
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", webtoonId=" + webtoonId +
                ", memberId=" + memberId +
                ", content='" + content + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }

}