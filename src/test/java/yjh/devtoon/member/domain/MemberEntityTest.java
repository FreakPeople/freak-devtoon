package yjh.devtoon.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("도메인 단위 테스트 [Member]")
class MemberEntityTest {

    @DisplayName("[create() 테스트] : 웹툰독자 엔티티 생성 테스트")
    @Test
    void createMember_successfully() {
        assertThatCode(() -> MemberEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL)
        ).doesNotThrowAnyException();
    }

    @DisplayName("[create() 테스트] : 웹툰독자 엔티티 생성 필드 테스트")
    @Test
    void createMember_successfully_and_validateField() {
        // when
        MemberEntity memberEntity = MemberEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL
        );

        // then
        assertAll(
                () -> assertThat(memberEntity.getName()).isEqualTo("name"),
                () -> assertThat(memberEntity.getEmail()).isEqualTo("email@gmail.com"),
                () -> assertThat(memberEntity.getPassword()).isEqualTo("password"),
                () -> assertThat(memberEntity.getMembershipStatus()).isEqualTo(MembershipStatus.GENERAL)
        );
    }

    @DisplayName("[change() 테스트] : 웹툰독자 등급 변경 테스트")
    @ParameterizedTest(name = "change to \"{0}\"")
    @EnumSource(value = MembershipStatus.class, names = {"GENERAL", "PREMIUM", "SUSPENDED"})
    void changeMembershipStatus_successfully(MembershipStatus status) {
        // given
        MemberEntity memberEntity = MemberEntity.create(
                "name",
                "email@gmail.com",
                "password",
                MembershipStatus.GENERAL
        );

        // when
        memberEntity.change(status);

        // then
        assertThat(memberEntity.getMembershipStatus()).isEqualTo(status);
    }

}