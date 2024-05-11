package yjh.devtoon.payment.dto.response;

import lombok.Getter;
import yjh.devtoon.payment.dto.CookiePaymentDetailDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CookiePaymentRetrieveResponse {

    private Long webtoonViewerNo;
    private Integer quantity;
    private BigDecimal totalPrice;         // 총 금액
    private BigDecimal totalDiscountRate;  // 총 할인율
    private BigDecimal paymentPrice;       // 결제 금액 = totalPrice * (1-totalDiscountRate)
    private LocalDateTime createdAt;

    public static CookiePaymentRetrieveResponse from(
            final CookiePaymentDetailDto cookiePaymentDetailDto
    ) {
        CookiePaymentRetrieveResponse response = new CookiePaymentRetrieveResponse();
        response.webtoonViewerNo = cookiePaymentDetailDto.getCookiePayment().getWebtoonViewerId();
        response.quantity = cookiePaymentDetailDto.getCookiePayment().getQuantity();
        response.totalPrice = cookiePaymentDetailDto.getTotalPrice().getAmount();
        response.totalDiscountRate = cookiePaymentDetailDto.getCookiePayment().getTotalDiscountRate();
        response.paymentPrice = cookiePaymentDetailDto.getPaymentPrice().getAmount();
        response.createdAt = cookiePaymentDetailDto.getCookiePayment().getCreatedAt();
        return response;
    }

}
