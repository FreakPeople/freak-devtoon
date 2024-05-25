package yjh.devtoon.webtoon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public WebtoonEntity(
            final Long id,
            final String title,
            final String writerName,
            final Genre genre,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.title = title;
        this.writerName = writerName;
        this.genre = genre;
        this.deletedAt = deletedAt;
    }

    public static WebtoonEntity create(
            final String title,
            final String writerName,
            final Genre genre
    ) {
        return WebtoonEntity.builder()
                .title(title)
                .writerName(writerName)
                .genre(genre)
                .build();
    }

    public boolean isGenre(final String attributeValue) {
        return genre.isSame(attributeValue);
    }

    public boolean isWriter(String name) {
        return writerName.equals(name);
    }

    @Override
    public String toString() {
        return "WebtoonEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", writerName='" + writerName + '\'' +
                ", genre=" + genre +
                ", deletedAt=" + deletedAt +
                '}';
    }

}
