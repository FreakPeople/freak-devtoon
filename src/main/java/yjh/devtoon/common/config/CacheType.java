package yjh.devtoon.common.config;

import lombok.Getter;

@Getter
public enum CacheType {

    PROMOTION ("promotion");

    private final String name;
    private final int expireAfterWrite;
    private final int maximumSize;

    CacheType(String name) {
        this.name = name;
        this.expireAfterWrite = ConstConfig.DEFAULT_TTL_SEC;
        this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
    }

    static class ConstConfig {
        static final int DEFAULT_TTL_SEC = 10;
        static final int DEFAULT_MAX_SIZE = 100;
    }

}
