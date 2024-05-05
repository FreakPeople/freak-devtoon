package yjh.devtoon.cookie_wallet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 [CookieWallet]")
class CookieWalletEntityTest {

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