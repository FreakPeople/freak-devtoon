package yjh.devtoon.promotion.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayName("도메인 단위 테스트 [Promotion]")
class PromotionEntityTest {

    @Test
    @DisplayName("[create() 테스트] : 프로모션 엔티티 생성 테스트")
    public void createPromotionEntityTest() {

        // given
        String description = "여름 프로모션입니다.";
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 30, 23, 59);

        // when
        PromotionEntity promotion = PromotionEntity.create(
                description, startDate, endDate
        );

        // then
        assertAll("promotion",
                () -> assertEquals(description, promotion.getDescription()),
                () -> assertEquals(startDate, promotion.getStartDate()),
                () -> assertEquals(endDate, promotion.getEndDate()),
                () -> assertNull(promotion.getDeletedAt())
        );

    }

}