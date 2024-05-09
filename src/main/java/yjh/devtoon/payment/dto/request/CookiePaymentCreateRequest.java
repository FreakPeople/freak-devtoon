package yjh.devtoon.payment.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CookiePaymentCreateRequest {

    private final Long webtoonViewerId;

    @Size(min = 1)
    private final Integer quantity;

}
