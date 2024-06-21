package yjh.devtoon.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import yjh.devtoon.common.exception.DevtoonException;

@DisplayName("도메인 단위 테스트 [MembershipStatus]]")
class MembershipStatusTest {

    @DisplayName("[create() 테스트] : 멤버십 등급 생성 성공 테스트")
    @ParameterizedTest(name = "{0} 등급 생성 테스트")
    @ValueSource(strings = {"general", "premium", "suspended"})
    void createMembershipStatus_successfully(String status) {
        // when
        MembershipStatus result = MembershipStatus.create(status);

        // then
        assertThat(result.getStatus()).isEqualTo(status);
    }

    @DisplayName("[create() 테스트] : 멤버십 등급 생성 실패 테스트")
    @ParameterizedTest(name = "{0} 등급 생성 실패 테스트")
    @ValueSource(strings = {" ", "not_exsist"})
    void givenNotExistMembershipName_whenCreateMembershipStatus_thenThrowException(String status) {
        assertThatThrownBy(() -> MembershipStatus.create(status))
                .isInstanceOf(DevtoonException.class);
    }

}