package yjh.devtoon.policy.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.policy.common.Policy;
import yjh.devtoon.policy.domain.PolicyFactory;
import yjh.devtoon.policy.domain.PolicyType;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.policy.infrastructure.PolicyRepositoryRegistry;
import yjh.devtoon.promotion.constant.ErrorMessage;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PolicyService {

    private final PolicyRepositoryRegistry policyRepositoryRegistry;

    /**
     * 정책 등록
     */
    @Transactional
    public void register(PolicyCreateRequest request) {
        // 1. 정책 확인 -> PolicyType 객체 반환
        PolicyType policyType = PolicyType.create(request.getPolicyName());
        log.info("policyType : {}", policyType);

        // 2. 정책 타입에 맞게 정책 객체(Policy)를 생성 및 반환
        Policy policy = PolicyFactory.valueOf(policyType.getPolicyName()).createPolicy(request);
        log.info("policy : {}", policy);

        // 3. 각 정책 리포지토리에 저장
        // @SuppressWarnings("unchecked") : 제네릭 타입 캐스팅 시 발생하는 경고 억제
        @SuppressWarnings("unchecked")
        JpaRepository<Policy, Long> repository = (JpaRepository<Policy, Long>)
                Optional.ofNullable(policyRepositoryRegistry.getRepository(policy.getClass()))
                        .orElseThrow(() -> new DevtoonException(
                                ErrorCode.NOT_FOUND,
                                ErrorMessage.getResourceNotFound(
                                        ResourceType.POLICY,
                                        policyType
                                )
                        ));
        repository.save(policy);
    }

}
