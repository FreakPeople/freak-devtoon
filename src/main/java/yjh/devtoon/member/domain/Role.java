package yjh.devtoon.member.domain;

public enum Role {
    MEMBER("member"),
    ADMIN("admin");

    private final String role;

    Role(final String role) {
        this.role = role;
    }
}
