package yjh.devtoon.bad_words_warning_count.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yjh.devtoon.member.domain.Authority;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.Role;
import java.time.LocalDateTime;
import java.util.Set;

@DisplayName("도메인 단위 테스트 [BadWordsWarningCount]")
class BadWordsWarningCountEntityTest {

    @DisplayName("[create() 테스트] : 비속어 카운트 엔티티 생성 테스트")
    @Test
    void createBadWordsWarningCount_successfully() {
        // given
        MemberEntity member = new MemberEntity(
                1L,
                "홍길동",
                "emai@gmail.com",
                "password",
                MembershipStatus.GENERAL,
                Set.of(new Authority(Role.MEMBER)),
                null
        );

        // when
        assertThatCode(() -> BadWordsWarningCountEntity.create(member)
        ).doesNotThrowAnyException();
    }

    @DisplayName("[increase() 테스트] : 비속어 카운트 증가 테스트")
    @Test
    void increaseBadWordsWarningCount_successfully() {
        // given
        BadWordsWarningCountEntity badWordsWarningCount = new BadWordsWarningCountEntity(
                1L,
                5,
                LocalDateTime.now()
        );

        // when
        BadWordsWarningCountEntity result = badWordsWarningCount.increase();

        // then
        assertAll(
                () -> assertThat(badWordsWarningCount.getCount()).isEqualTo(6),
                () -> assertThat(result.getCount()).isEqualTo(6)
        );
    }

}