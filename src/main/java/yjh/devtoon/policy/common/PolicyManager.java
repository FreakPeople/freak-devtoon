package yjh.devtoon.policy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 모든 정책을 관리하고 적용합니다.
 */
@Slf4j
@Component
public class PolicyManager {

    private List<Policy> policies = new ArrayList<>();

    public void addPolicy(Policy policy) {
        policies.add(policy);
    }

    public List<Policy> getActivePolicies() {
        LocalDateTime now = LocalDateTime.now();
        return policies.stream()
                .filter(policy -> isActive(policy, now))
                .collect(Collectors.toList());
    }

    private boolean isActive(Policy policy, LocalDateTime now) {
        return !now.isBefore(policy.getStartDate()) && (policy.getEndDate() == null || now.isBefore(policy.getEndDate()));
    }

    public void applyAllActivePolicies() {
        List<Policy> activePolicies = getActivePolicies();
        for (Policy policy : activePolicies) {
            policy.applyPolicy();
        }
    }

}