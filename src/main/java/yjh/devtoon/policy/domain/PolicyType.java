package yjh.devtoon.policy.domain;

import lombok.Getter;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import java.util.Arrays;

/**
 * 정책 타입을 관리
 * - 새로운 Policy 타입이 추가될 때 해당 클래스에 등록
 */
@Getter
public enum PolicyType {
    COOKIE_POLICY("COOKIE_POLICY"),
    BAD_WORDS_POLICY("BAD_WORDS_POLICY");

    private final String policyName;

    PolicyType(String policyName) {
        this.policyName = policyName;
    }

    /**
     * policyName 문자열과 일치하는 PolicyType 객체를 반환
     */
    public static PolicyType create(String policyName) {
        return Arrays.stream(PolicyType.values())
                .filter(p -> p.getPolicyName().equals(policyName))
                .findFirst()
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, policyName));
    }

}
