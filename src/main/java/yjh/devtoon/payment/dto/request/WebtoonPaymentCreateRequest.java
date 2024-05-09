package yjh.devtoon.payment.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WebtoonPaymentCreateRequest {

    private final Long webtoonViewerId;

    private final Long webtoonId;

    private final Long webtoonDetailId;

}
