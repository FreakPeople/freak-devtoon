package yjh.devtoon.promotion.domain;

import lombok.Getter;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.promotion.constant.ErrorMessage;
import java.util.Arrays;

@Getter
public enum AttributeName {

    TARGET_AUTHOR("target_author"),
    TARGET_GENRE("target_genre"),
    TARGET_MONTH("target_month"),
    RELEASE_MONTH("release_month"),
    COOKIE_PURCHASE_QUANTITY("cookie_purchase_quantity"),
    PREMIUM_MEMBER_DISCOUNT("premium_member_discount"),
    PREVIOUS_MONTH_COOKIE_PURCHASE("previous_month_cookie_purchase");

    private final String name;

    AttributeName(String name) {
        this.name = name;
    }

    public static AttributeName create(final String attributeName) {
        return Arrays.stream(AttributeName.values())
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.PROMOTION, attributeName)));
    }

}
