package yjh.devtoon.payment.dto.response;

import lombok.Getter;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CookiePaymentRetrieveResponse {

    private Long webtoonViewerNo;
    private Integer quantity;
    private BigDecimal totalPrice;         // 총 금액
    private BigDecimal totalDiscountRate;  // 총 할인율
    private BigDecimal paymentPrice;       // 결제 금액 = totalPrice * (1-totalDiscountRate)
    private LocalDateTime createAt;

    public static CookiePaymentRetrieveResponse from(final CookiePaymentEntity cookiePayment) {
        CookiePaymentRetrieveResponse response = new CookiePaymentRetrieveResponse();

        response.webtoonViewerNo = cookiePayment.getWebtoonViewerId();
        response.quantity = cookiePayment.getQuantity();

        BigDecimal quantityAsBigDecimal = new BigDecimal(cookiePayment.getQuantity());
        response.totalPrice = cookiePayment.getCookiePrice().multiply(quantityAsBigDecimal);

        response.totalDiscountRate = cookiePayment.getTotalDiscountRate();

        BigDecimal finalPaymentRate = BigDecimal.ONE.subtract(response.totalDiscountRate);
        response.paymentPrice = response.totalPrice.multiply(finalPaymentRate);

        response.createAt = LocalDateTime.now();

        return response;
    }

}
