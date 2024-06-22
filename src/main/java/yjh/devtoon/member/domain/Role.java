package yjh.devtoon.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    private final String role;

    Role(final String role) {
        this.role = role;
    }
}
