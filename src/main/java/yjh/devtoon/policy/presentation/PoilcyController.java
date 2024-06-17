package yjh.devtoon.policy.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.policy.application.PolicyService;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.policy.dto.response.RetrieveCookiePolicyResponse;

@Slf4j
@RequestMapping("/v1/policies")
@RequiredArgsConstructor
@RestController
public class PoilcyController {

    private final PolicyService policyService;

    /**
     * 정책 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse> register(
            @RequestBody final PolicyCreateRequest request
    ) {
        policyService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 쿠키 정책 조회
     */
    @GetMapping("/cookie-policy")
    public ResponseEntity<ApiResponse> retrieveCookiePolicy() {
        CookiePolicyEntity cookiePolicy = policyService.retrieveCookiePolicy();
        RetrieveCookiePolicyResponse response = RetrieveCookiePolicyResponse.from(cookiePolicy);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
