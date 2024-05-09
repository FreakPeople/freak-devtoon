package yjh.devtoon.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.Response;
import yjh.devtoon.payment.application.CookiePaymentService;
import yjh.devtoon.payment.dto.request.CookiePaymentCreateRequest;

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
    public ResponseEntity<Response> register(
            @RequestBody final CookiePaymentCreateRequest request
    ) {
        cookiePaymentService.register(request);
        return ResponseEntity.ok(Response.success(null));
    }

}
