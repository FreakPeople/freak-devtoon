package yjh.devtoon.cookie_wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CookieRequest {

    private int quantity;

    public CookieRequest(int quantity) {
        this.quantity = quantity;
    }

}