package yjh.devtoon.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiReponse;
import yjh.devtoon.payment.application.CookiePaymentService;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import yjh.devtoon.payment.dto.request.CookiePaymentCreateRequest;
import yjh.devtoon.payment.dto.response.CookiePaymentRetrieveResponse;

@RequestMapping("/v1/cookie-payments")
@RequiredArgsConstructor
@RestController
public class CookiePaymentController {

    private final CookiePaymentService cookiePaymentService;

    /**
     * 쿠키 결제
     * : 쿠키는 현금으로 결제한다.
     */
    @PostMapping
    public ResponseEntity<ApiReponse> register(
            @RequestBody final CookiePaymentCreateRequest request
    ) {
        cookiePaymentService.register(request);
        return ResponseEntity.ok(ApiReponse.success(null));
    }

    /**
     * 특정 회원 쿠키 결제 내역 단건 조회
     */
    @GetMapping("/{webtoonViewerId}")
    public ResponseEntity<ApiReponse> retrieve(
            @PathVariable final Long webtoonViewerId
    ) {
        CookiePaymentEntity cookiePayment = cookiePaymentService.retrieve(webtoonViewerId);
        CookiePaymentRetrieveResponse response = CookiePaymentRetrieveResponse.from(cookiePayment);
        return ResponseEntity.ok(ApiReponse.success(response));
    }

}
