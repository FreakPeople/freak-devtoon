package yjh.devtoon.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import yjh.devtoon.payment.domain.Price;

@RequiredArgsConstructor
@Getter
public class CookiePaymentDetailDto {

    private final CookiePaymentEntity cookiePayment;
    private final Price totalPrice;
    private final Price paymentPrice;

    public static CookiePaymentDetailDto from(final CookiePaymentEntity cookiePayment) {
        return new CookiePaymentDetailDto(
                cookiePayment,
                cookiePayment.calculateTotalPrice(),
                cookiePayment.calculatePaymentPrice()
        );
    }

}
