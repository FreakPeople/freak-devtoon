package yjh.devtoon.policy.common;

import java.time.LocalDateTime;

public interface Policy {

    /**
     * 정책의 고유 식별자를 반환합니다.
     */
    Long getId();

    /**
     * 정책이 유효해지는 시작 날짜를 반환합니다.
     */
    LocalDateTime getStartDate();

    /**
     * 정책이 더 이상 유효하지 않은 종료 날짜를 반환합니다.
     */
    LocalDateTime getEndDate();

    /**
     * 정책을 적용합니다.
     */
    void applyPolicy();

}