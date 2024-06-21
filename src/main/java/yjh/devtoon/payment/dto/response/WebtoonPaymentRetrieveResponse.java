package yjh.devtoon.payment.dto.response;

import lombok.Getter;
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;
import java.time.LocalDateTime;

@Getter
public class WebtoonPaymentRetrieveResponse {

    private Long memberId;
    private Long webtoonNo;
    private Long webtoonDetailNo;
    private Long cookiePaymentAmount;
    private LocalDateTime createdAt;

    public static WebtoonPaymentRetrieveResponse from(final WebtoonPaymentEntity webtoonPayment) {
        WebtoonPaymentRetrieveResponse response = new WebtoonPaymentRetrieveResponse();
        response.memberId = webtoonPayment.getMemberId();
        response.webtoonNo = webtoonPayment.getWebtoonId();
        response.webtoonDetailNo = webtoonPayment.getWebtoonDetailId();
        response.cookiePaymentAmount = webtoonPayment.getCookiePaymentAmount();
        response.createdAt = webtoonPayment.getCreatedAt();
        return response;
    }

}
