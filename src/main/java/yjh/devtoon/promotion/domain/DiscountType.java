package yjh.devtoon.promotion.domain;

import lombok.Getter;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.promotion.constant.ErrorMessage;
import java.util.Arrays;

@Getter
public enum DiscountType {
    CASH_DISCOUNT("cash_discount"),
    COOKIE_QUANTITY_DISCOUNT("cookie_quantity_discount");

    private final String discountType;

    DiscountType(String discountType) {
        this.discountType = discountType;
    }

    public static DiscountType create(final String discountType) {
        return Arrays.stream(DiscountType.values())
                .filter(t -> t.getDiscountType().equals(discountType))
                .findFirst()
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(
                                ResourceType.PROMOTION,
                                discountType
                        )
                ));
    }

}
