package yjh.devtoon.payment.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.payment.application.CookiePaymentService;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import yjh.devtoon.payment.dto.CookiePaymentDetailDto;
import yjh.devtoon.payment.dto.request.CookiePaymentCreateRequest;
import yjh.devtoon.payment.dto.response.CookiePaymentRetrieveResponse;

@Slf4j
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
    public ResponseEntity<ApiResponse> register(
            @RequestBody final CookiePaymentCreateRequest request
    ) {
        cookiePaymentService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 특정 회원 쿠키 결제 내역 단건 조회
     */
    @GetMapping("/{webtoonViewerId}")
    public ResponseEntity<ApiResponse> retrieve(
            @PathVariable final Long webtoonViewerId
    ) {
        CookiePaymentEntity cookiePayment = cookiePaymentService.retrieve(webtoonViewerId);
        CookiePaymentDetailDto cookiePaymentDetailDto = CookiePaymentDetailDto.from(cookiePayment);
        CookiePaymentRetrieveResponse response =
                CookiePaymentRetrieveResponse.from(cookiePaymentDetailDto);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
