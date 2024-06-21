package yjh.devtoon.cookie_wallet.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yjh.devtoon.member.domain.Authority;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.Role;
import java.util.Set;

@DisplayName("도메인 단위 테스트 [CookieWallet]")
class CookieWalletEntityTest {

    @DisplayName("[create() 테스트] : 쿠키 지갑 엔티티 생성 테스트")
    @Test
    void createCookieWallet_successfully() {
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
        assertThatCode(() -> CookieWalletEntity.create(member)
        ).doesNotThrowAnyException();
    }

    @DisplayName("[increase() 테스트] : 쿠기 증가 테스트")
    @Test
    void increaseCookieQuantity_successfully() {
        // given
        CookieWalletEntity cookieWallet = new CookieWalletEntity(
                1L,
                10,
                null
        );

        // when
        cookieWallet.increase(5);

        // then
        assertThat(cookieWallet.getQuantity()).isEqualTo(15);
    }

    @DisplayName("[decrease() 테스트] : 쿠기 감소 테스트")
    @Test
    void decreaseCookieQuantity_successfully() {
        // given
        CookieWalletEntity cookieWallet = new CookieWalletEntity(
                1L,
                10,
                null
        );

        // when
        cookieWallet.decrease(5);

        // then
        assertThat(cookieWallet.getQuantity()).isEqualTo(5);
    }
}