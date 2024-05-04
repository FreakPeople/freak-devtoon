package yjh.devtoon.bad_words_warning_count.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import java.time.LocalDateTime;

@DisplayName("도메인 단위 테스트 [BadWordsWarningCount]")
class BadWordsWarningCountEntityTest {

    @DisplayName("[create() 테스트] : 비속어 카운트 엔티티 생성 테스트")
    @Test
    void createBadWordsWarningCount_successfully() {
        // given
        WebtoonViewerEntity webtoonViewer = new WebtoonViewerEntity(
                1L,
                "홍길동",
                "emai@gmail.com",
                "password",
                MembershipStatus.GENERAL,
                null
        );

        // when
        assertThatCode(() -> BadWordsWarningCountEntity.create(webtoonViewer)
        ).doesNotThrowAnyException();
    }
}