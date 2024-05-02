package yjh.devtoon.webtoon.domain;

import lombok.Getter;

@Getter
public enum MembershipStatus {
    GENERAL("general"),
    PREMIUM("premium"),
    SUSPENDED("suspended");

    private final String status;

    MembershipStatus(final String status) {
        this.status = status;
    }

}
