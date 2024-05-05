package yjh.devtoon.cookie_wallet.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;

@RequiredArgsConstructor
@Getter
public class CookieWalletResponse {

    private final Long webtoonViewerNo;
    private final int quantity;

    public static CookieWalletResponse from(final CookieWalletEntity cookieWallet) {
        return new CookieWalletResponse(
                cookieWallet.getWebtoonViewerId(),
                cookieWallet.getQuantity()
        );
    }

}