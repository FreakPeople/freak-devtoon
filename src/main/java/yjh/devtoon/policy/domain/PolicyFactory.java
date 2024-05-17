package yjh.devtoon.policy.domain;

import lombok.Getter;
import yjh.devtoon.policy.common.Policy;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;

/**
 * 정책 타입에 맞게 정책 객체를 생성 및 반환
 */
@Getter
public enum PolicyFactory {

    COOKIE_POLICY("COOKIE_POLICY", PolicyType.COOKIE_POLICY) {
        @Override
        public Policy createPolicy(PolicyCreateRequest request) {
            CookiePolicyEntity cookiePolicy = CookiePolicyEntity.create(
                    request.getCookiePrice(),
                    request.getCookieQuantityPerEpisode(),
                    request.getStartDate(),
                    request.getEndDate()
            );
            return cookiePolicy;
        }
    },
    BAD_WORDS_POLICY("BAD_WORDS_POLICY", PolicyType.BAD_WORDS_POLICY) {
        @Override
        public Policy createPolicy(PolicyCreateRequest request) {
            BadWordsPolicyEntity badWordsPolicy = BadWordsPolicyEntity.create(
                    request.getWarningThreshold(),
                    request.getStartDate(),
                    request.getEndDate()
            );
            return badWordsPolicy;
        }
    };

    private final String policyName;
    private final PolicyType policyType;

    PolicyFactory(String policyName, PolicyType policyType) {
        this.policyName = policyName;
        this.policyType = policyType;
    }

    public abstract Policy createPolicy(PolicyCreateRequest request);

}
