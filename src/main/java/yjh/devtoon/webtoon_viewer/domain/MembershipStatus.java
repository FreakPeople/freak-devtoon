package yjh.devtoon.webtoon_viewer.domain;

import lombok.Getter;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon_viewer.constant.ErrorMessage;
import java.util.Arrays;

@Getter
public enum MembershipStatus {
    GENERAL("general"),
    PREMIUM("premium"),
    SUSPENDED("suspended");

    private final String status;

    MembershipStatus(final String status) {
        this.status = status;
    }

    /**
     * status = "general", "premium"...
     */
    public static MembershipStatus create(final String status) {
        return Arrays.stream(MembershipStatus.values())
                .filter(m -> m.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getMembershipStatusNotFound(status))
                );
    }

}
