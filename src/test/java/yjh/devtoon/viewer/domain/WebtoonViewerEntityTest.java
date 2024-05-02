package yjh.devtoon.viewer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 [WebtoonViewer]")
class WebtoonViewerEntityTest {

    @DisplayName("[create() 테스트] : 웹툰독자 엔티티 생성 테스트")
    @Test
    void createWebtoonViewer_successfully() {
        assertThatCode(() -> WebtoonViewerEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL)
        ).doesNotThrowAnyException();
    }

    @DisplayName("[create() 테스트] : 웹툰독자 엔티티 생성 필드 테스트")
    @Test
    void createWebtoonViewer_successfully_and_validateField() {
        // when
        WebtoonViewerEntity webtoonViewerEntity = WebtoonViewerEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL
        );

        // then
        assertAll(
                () -> assertThat(webtoonViewerEntity.getName()).isEqualTo("name"),
                () -> assertThat(webtoonViewerEntity.getEmail()).isEqualTo("email@gmail.com"),
                () -> assertThat(webtoonViewerEntity.getPassword()).isEqualTo("password"),
                () -> assertThat(webtoonViewerEntity.getMembershipStatus()).isEqualTo(MembershipStatus.GENERAL)
        );
    }
}