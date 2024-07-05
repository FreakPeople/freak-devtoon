package yjh.devtoon.cookie_wallet.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.cookie_wallet.application.CookieWalletService;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.dto.request.CookieRequest;
import yjh.devtoon.cookie_wallet.dto.response.CookieWalletResponse;

@RequestMapping("/v1/cookie-wallets")
@RequiredArgsConstructor
@RestController
public class CookieWalletController {

    private final CookieWalletService cookieWalletService;

    /**
     * 쿠키 지갑 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> retrieve(
            @RequestParam("memberId") final Long id
    ) {
        CookieWalletEntity cookieWallet = cookieWalletService.retrieve(id);
        CookieWalletResponse response = CookieWalletResponse.from(cookieWallet);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /**
     * 내 쿠키 지갑 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> retrieveMyInfo(Authentication authentication) {
        String memberEmail = authentication.getName();

        CookieWalletEntity cookieWallet = cookieWalletService.retrieveMyInfo(memberEmail);
        CookieWalletResponse response = CookieWalletResponse.from(cookieWallet);
        return ResponseEntity.ok(ApiResponse.success(response));
    }



    /**
     * 쿠키 증가
     */
    @PutMapping("/increase")
    public ResponseEntity<ApiResponse> increase(
            @RequestParam("memberId") final Long id,
            @RequestBody final CookieRequest request
    ) {
        CookieWalletEntity cookieWallet = cookieWalletService.increase(id, request);
        CookieWalletResponse response = CookieWalletResponse.from(cookieWallet);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 쿠키 감소
     */
    @PutMapping("/decrease")
    public ResponseEntity<ApiResponse> decrease(
            @RequestParam("memberId") final Long id,
            @RequestBody final CookieRequest request
    ) {
        CookieWalletEntity cookieWallet = cookieWalletService.decrease(id, request);
        CookieWalletResponse response = CookieWalletResponse.from(cookieWallet);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}