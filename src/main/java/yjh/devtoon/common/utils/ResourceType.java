package yjh.devtoon.common.utils;

import lombok.Getter;

@Getter
public enum ResourceType {
    BAD_WORDS_WARNING_COUNT("BadWordsWarningCount"),
    COMMENT("Comment"),
    COOKIE_WALLET("CookieWallet"),
    COOKIE_PAYMENT("CookiePayment"),
    WEBTOON_PAYMENT("WebtoonPayment"),
    POLICY("Policy"),
    PROMOTION("Promotion"),
    WEBTOON("Webtoon"),
    WEBTOON_VIEWER("WebtoonViewer");

    private final String resourceName;

    ResourceType(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

}
