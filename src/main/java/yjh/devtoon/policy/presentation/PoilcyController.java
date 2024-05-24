package yjh.devtoon.policy.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.policy.application.PolicyService;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;

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
     * TODO: 활성 정책 조회
     * : 현재 시간 기준으로 활성화된 정책들을 조회합니다.
     */

}
