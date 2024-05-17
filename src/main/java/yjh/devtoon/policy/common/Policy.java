package yjh.devtoon.policy.common;

import java.time.LocalDateTime;

public interface Policy {

    // 정책 고유 id
    Long getId();

    // 정책 효력 발생 시작 시간
    LocalDateTime getStartDate();

    // 정책 효력 발생 끝 시간
    LocalDateTime getEndDate();

}