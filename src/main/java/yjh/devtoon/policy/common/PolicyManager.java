package yjh.devtoon.policy.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.infrastructure.BadWordsPolicyRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 모든 정책을 관리하고 적용합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PolicyManager {

    private final CookiePolicyRepository cookiePolicyRepository;
    private final BadWordsPolicyRepository badWordsPolicyRepository;

    private List<Policy> policies = new ArrayList<>();

    public void addPolicy(Policy policy) {
        policies.add(policy);
    }

    public List<Policy> getActivePolicies() {

        LocalDateTime now = LocalDateTime.now();
        List<Policy> activePolicies = new ArrayList<>();

        List<CookiePolicyEntity> cookiePolicies = cookiePolicyRepository.findActivePolicies(now);
        List<BadWordsPolicyEntity> badWordsPolicies = badWordsPolicyRepository.findActivePolicies(now);

        activePolicies.addAll(cookiePolicies);
        activePolicies.addAll(badWordsPolicies);

        return activePolicies;
    }

    public void applyAllActivePolicies() {
        List<Policy> activePolicies = getActivePolicies();
        for (Policy policy : activePolicies) {
            policy.applyPolicy();
        }
    }

}