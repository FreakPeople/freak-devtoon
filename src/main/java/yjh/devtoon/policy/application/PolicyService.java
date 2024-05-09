package yjh.devtoon.policy.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.policy.common.Policy;
import yjh.devtoon.policy.common.PolicyManager;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.domain.factory.PolicyFactory;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.policy.dto.response.RetrieveActivePoliciesResponse;
import yjh.devtoon.policy.infrastructure.BadWordsPolicyRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.promotion.constant.ErrorMessage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PolicyService {

    private final CookiePolicyRepository cookiePolicyRepository;
    private final BadWordsPolicyRepository badWordsPolicyRepository;
    private final PolicyManager policyManager;

    /**
     * 정책 등록
     * : 다양한 유형의 정책 등록 후 바로 적용합니다.
     */
    @Transactional
    public Long register(PolicyCreateRequest request) {

        Policy policy = PolicyFactory.registerPolicy(request);
        savePolicyByType(policy);

        policyManager.addPolicy(policy);
        log.info("정책매니저에 새로운 정책 등록: {}", policy);

        policyManager.applyAllActivePolicies();
        log.info("정책매니저에 모든 활성 정책을 적용: {}", policy);

        return policy.getId();

    }

    private void savePolicyByType(Policy policy) {

        if (policy instanceof CookiePolicyEntity) {
            cookiePolicyRepository.save((CookiePolicyEntity) policy);
            log.info("쿠키 정책 저장: {}", policy);
        } else if (policy instanceof BadWordsPolicyEntity) {
            badWordsPolicyRepository.save((BadWordsPolicyEntity) policy);
            log.info("비속어 정책 저장: {}", policy);
        } else {
            throw new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("Policy", policy));
        }

    }

    @Transactional(readOnly = true)
    public RetrieveActivePoliciesResponse getActivePolicies() {

        List<RetrieveActivePoliciesResponse.PolicyDetailsInfo> activePoliciesList = policyManager.getActivePolicies()
                .stream()
                .map(p -> new RetrieveActivePoliciesResponse.PolicyDetailsInfo(p.getId(), p.getClass().getSimpleName(), p.toString()))
                .collect(Collectors.toList());

        return new RetrieveActivePoliciesResponse(activePoliciesList);

    }

}
