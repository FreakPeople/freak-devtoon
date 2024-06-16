package yjh.devtoon.webtoon_viewer.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class WebtoonViewerResponse {

    private final String name;
    private final String email;
    private final MembershipStatus membershipStatus;
    private final LocalDateTime createdAt;

    public static WebtoonViewerResponse from(final WebtoonViewerEntity webtoonViewer) {
        return new WebtoonViewerResponse(
                webtoonViewer.getName(),
                webtoonViewer.getEmail(),
                webtoonViewer.getMembershipStatus(),
                webtoonViewer.getCreatedAt()
        );
    }

}
