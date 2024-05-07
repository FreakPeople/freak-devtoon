package yjh.devtoon.policy.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.Response;
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
     * : 다양한 유형의 정책 등록 후 바로 적용합니다.
     */
    @PostMapping
    public ResponseEntity<Response<Long>> register(
            @RequestBody final PolicyCreateRequest request
    ) {
        Long policyId = policyService.register(request);
        log.info("새로운 정책 등록: ID={}", policyId);

        return ResponseEntity.ok(Response.success(policyId));
    }

}
