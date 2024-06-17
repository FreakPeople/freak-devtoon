package yjh.devtoon.policy.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.utils.ResourceType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

    private static final String RESOURCE_NOT_FOUND = "%s : '%s' 을/를 찾을 수 없습니다.";

    public static String getResourceNotFound(
            final ResourceType resourceType,
            final Object identifier
    ) {
        return String.format(RESOURCE_NOT_FOUND, resourceType, identifier);
    }

}
