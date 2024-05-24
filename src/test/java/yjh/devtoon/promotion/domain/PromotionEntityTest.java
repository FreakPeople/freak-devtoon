package yjh.devtoon.promotion.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@DisplayName("도메인 단위 테스트 [Promotion]")
class PromotionEntityTest {
    private static final String DESCRIPTION = "7월 스릴러 장르 파격 할인 행사입니다.";
    private static final DiscountType DISCOUNT_TYPE = DiscountType.COOKIE_QUANTITY_DISCOUNT;
    private static final BigDecimal DISCOUNT_RATE = null;
    private static final Integer DISCOUNT_QUANTITY = 1;
    private static final Boolean IS_DISCOUNT_DUPLICATABLE = true;
    private static final LocalDateTime STARTDATE = LocalDateTime.of(2024, 7, 15, 0, 0, 0);
    private static final LocalDateTime ENDDATE = LocalDateTime.of(2024, 8, 15, 11, 59, 59);

    @Test
    @DisplayName("[create() 테스트] : 프로모션 엔티티 생성 테스트")
    public void createPromotionEntityTest() {
        // when
        PromotionEntity promotion = PromotionEntity.create(
                DESCRIPTION,
                DISCOUNT_TYPE,
                DISCOUNT_RATE,
                DISCOUNT_QUANTITY,
                IS_DISCOUNT_DUPLICATABLE,
                STARTDATE,
                ENDDATE
        );

        // then
        assertAll("promotion",
                () -> assertEquals(DESCRIPTION, promotion.getDescription()),
                () -> assertEquals(DISCOUNT_TYPE, promotion.getDiscountType()),
                () -> assertEquals(DISCOUNT_RATE, promotion.getDiscountRate()),
                () -> assertEquals(DISCOUNT_QUANTITY, promotion.getDiscountQuantity()),
                () -> assertEquals(IS_DISCOUNT_DUPLICATABLE, promotion.getIsDiscountDuplicatable()),
                () -> assertEquals(STARTDATE, promotion.getStartDate()),
                () -> assertEquals(ENDDATE, promotion.getEndDate()),
                () -> assertNull(promotion.getDeletedAt())
        );

    }

}