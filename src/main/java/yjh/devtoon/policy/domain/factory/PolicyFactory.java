package yjh.devtoon.policy.domain.factory;

import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.policy.common.Policy;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.promotion.constant.ErrorMessage;

public class PolicyFactory {

    private static final String POLICY = "Policy";

    public static Policy registerPolicy(PolicyCreateRequest request) {

        switch (request.getType()) {
            case "cookie":
                return new CookiePolicyEntity(request.getDetails());
            case "bad_words":
                return new BadWordsPolicyEntity(request.getDetails());
            default:
                throw new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(POLICY, request.getType()));
        }

    }

}
