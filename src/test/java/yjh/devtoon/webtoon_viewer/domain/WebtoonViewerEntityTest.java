package yjh.devtoon.webtoon_viewer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.Month;

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

    @DisplayName("[change() 테스트] : 웹툰독자 등급 변경 테스트")
    @ParameterizedTest(name = "change to \"{0}\"")
    @EnumSource(value = MembershipStatus.class, names = {"GENERAL", "PREMIUM", "SUSPENDED"})
    void changeWebtoonViewerMembershipStatus_successfully(MembershipStatus status) {
        // given
        WebtoonViewerEntity webtoonViewerEntity = WebtoonViewerEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL
        );

        // when
        webtoonViewerEntity.change(status);

        // then
        assertThat(webtoonViewerEntity.getMembershipStatus()).isEqualTo(status);
    }

}