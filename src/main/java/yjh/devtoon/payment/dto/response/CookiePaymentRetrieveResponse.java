package yjh.devtoon.payment.dto.response;

import lombok.Getter;
import yjh.devtoon.payment.dto.CookiePaymentDetailDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CookiePaymentRetrieveResponse {

    private Long memberId;
    private Integer quantity;
    private BigDecimal totalPrice;         // 총 금액
    private BigDecimal totalDiscountRate;  // 총 할인율
    private BigDecimal paymentPrice;       // 결제 금액 = totalPrice * (1-totalDiscountRate)
    private LocalDateTime createdAt;

    public CookiePaymentRetrieveResponse(
            Long memberId,
            Integer quantity,
            BigDecimal totalPrice,
            BigDecimal totalDiscountRate,
            BigDecimal paymentPrice,
            LocalDateTime createdAt
    ) {
        this.memberId = memberId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.totalDiscountRate = totalDiscountRate;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
    }

    public static CookiePaymentRetrieveResponse from(
            final CookiePaymentDetailDto cookiePaymentDetailDto
    ) {
        return new CookiePaymentRetrieveResponse(
                cookiePaymentDetailDto.getCookiePayment().getMemberId(),
                cookiePaymentDetailDto.getCookiePayment().getQuantity(),
                cookiePaymentDetailDto.getTotalPrice().getAmount(),
                cookiePaymentDetailDto.getCookiePayment().getTotalDiscountRate(),
                cookiePaymentDetailDto.getPaymentPrice().getAmount(),
                cookiePaymentDetailDto.getCookiePayment().getCreatedAt()
        );
    }

}
