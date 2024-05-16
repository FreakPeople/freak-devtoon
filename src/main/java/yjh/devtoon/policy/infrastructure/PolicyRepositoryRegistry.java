package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import yjh.devtoon.policy.common.Policy;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * 정책 관련 모든 리포지토리를 중앙에서 관리
 * - 새로운 Policy 타입이 추가될 때 해당 레지스트리에 새로운 리포지토리를 등록
 */
@Component
public class PolicyRepositoryRegistry {

    private final Map<Class<? extends Policy>, JpaRepository<? extends Policy, Long>> registry =
            new HashMap<>();

    public PolicyRepositoryRegistry(
            CookiePolicyRepository cookiePolicyRepository,
            BadWordsPolicyRepository badWordsPolicyRepository) {
        registry.put(CookiePolicyEntity.class, cookiePolicyRepository);
        registry.put(BadWordsPolicyEntity.class, badWordsPolicyRepository);
    }

    @SuppressWarnings("unchecked")
    public <T extends Policy> JpaRepository<T, Long> getRepository(Class<T> policyClass) {
        return (JpaRepository<T, Long>) registry.get(policyClass);
    }

}
