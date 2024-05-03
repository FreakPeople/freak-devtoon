package yjh.devtoon.webtoon_viewer.domain;

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
@Table(name = "webtoon_viewer")
public class WebtoonViewerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_viewer_no", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "membership_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Builder
    public WebtoonViewerEntity(
            final Long id,
            final String name,
            final String email,
            final String password,
            final MembershipStatus membershipStatus,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.membershipStatus = membershipStatus;
        this.deletedAt = deletedAt;
    }

    public static WebtoonViewerEntity create(
            final String name,
            final String email,
            final String password,
            final MembershipStatus membershipStatus
    ) {
        WebtoonViewerEntity webtoonViewer = new WebtoonViewerEntity();
        webtoonViewer.name = name;
        webtoonViewer.email = email;
        webtoonViewer.password = password;
        webtoonViewer.membershipStatus = membershipStatus;
        return webtoonViewer;
    }

    public void change(final MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

}
