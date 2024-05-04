package yjh.devtoon.webtoon_viewer.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;

@RequiredArgsConstructor
@Getter
public class WebtoonViewerResponse {

    private final String name;
    private final MembershipStatus membershipStatus;

    public static WebtoonViewerResponse from(final WebtoonViewerEntity webtoonViewer) {
        return new WebtoonViewerResponse(
                webtoonViewer.getName(),
                webtoonViewer.getMembershipStatus()
        );
    }

}
