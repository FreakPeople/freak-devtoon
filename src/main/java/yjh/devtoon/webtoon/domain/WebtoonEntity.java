package yjh.devtoon.webtoon.domain;

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
import yjh.devtoon.common.BaseEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "webtoon")
public class WebtoonEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_no", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "writerName", nullable = false)
    private String writerName;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public WebtoonEntity(
            final Long id,
            final String title,
            final String writerName,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.title = title;
        this.writerName = writerName;
        this.deletedAt = deletedAt;
    }

    public static WebtoonEntity create(final WebtoonCreateRequest webtoonCreateRequest) {
        return WebtoonEntity.builder()
                .title(webtoonCreateRequest.getTitle())
                .writerName(webtoonCreateRequest.getWriterName())
                .build();
    }

}
