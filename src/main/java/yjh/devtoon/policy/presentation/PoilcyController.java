package yjh.devtoon.policy.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiReponse;
import yjh.devtoon.policy.application.PolicyService;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.policy.dto.response.RetrieveActivePoliciesResponse;

@Slf4j
@RequestMapping("/v1/policies")
@RequiredArgsConstructor
@RestController
public class PoilcyController {

    private final PolicyService policyService;

    /**
     * 정책 등록
     * : 다양한 유형의 정책 등록 후 바로 적용합니다.
     */
    @PostMapping
    public ResponseEntity<ApiReponse<Long>> register(
            @RequestBody final PolicyCreateRequest request
    ) {
        Long policyId = policyService.register(request);
        log.info("새로운 정책 등록: ID={}", policyId);

        return ResponseEntity.ok(ApiReponse.success(policyId));
    }

    /**
     * 활성 정책 조회
     * : 현재 시간 기준으로 활성화된 정책들을 조회합니다.
     */
    @GetMapping("/active")
    public ResponseEntity<ApiReponse<RetrieveActivePoliciesResponse>> getActivePolicies() {
        RetrieveActivePoliciesResponse response = policyService.getActivePolicies();
        log.info("활성 정책 조회: {}", response);

        return ResponseEntity.ok(ApiReponse.success(response));
    }

}
