package yjh.devtoon.promotion.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yjh.devtoon.promotion.domain.DiscountType;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionAttributeCreateRequest;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionAttributeRepository promotionAttributeRepository;

    @Mock
    private PromotionCacheService promotionCacheService;

    @Captor
    private ArgumentCaptor<PromotionEntity> promotionEntityCaptor;

    @Nested
    @DisplayName("프로모션 등록 테스트")
    class RegisterPromotionTests {
        private static final String DESCRIPTION = "7월 스릴러 장르 파격 할인 행사입니다.";
        private static final DiscountType DISCOUNT_TYPE = DiscountType.COOKIE_QUANTITY_DISCOUNT;
        private static final BigDecimal DISCOUNT_RATE = null;
        private static final Integer DISCOUNT_QUANTITY = 1;
        private static final Boolean IS_DISCOUNT_DUPLICATABLE = true;
        private static final LocalDateTime STARTDATE = LocalDateTime.of(2024, 5, 13, 13, 50, 0);
        private static final LocalDateTime ENDDATE = LocalDateTime.of(2024, 8, 15, 11, 59, 59);
        private static final List<PromotionAttributeCreateRequest> PROMOTION_ATTRIBUTES = List.of(
                new PromotionAttributeCreateRequest("target-month", "7"),
                new PromotionAttributeCreateRequest("target-genre", "thriller")
        );

        @Test
        @DisplayName("프로모션 등록 요청 시 성공적으로 등록")
        void registerPromotion_successfully() {
            // given
            PromotionCreateRequest request = new PromotionCreateRequest(
                    DESCRIPTION,
                    DISCOUNT_TYPE,
                    DISCOUNT_RATE,
                    DISCOUNT_QUANTITY,
                    IS_DISCOUNT_DUPLICATABLE,
                    STARTDATE,
                    ENDDATE,
                    PROMOTION_ATTRIBUTES
            );

            // mocking
            when(promotionRepository.save(any(PromotionEntity.class))).thenAnswer(invocation -> {
                PromotionEntity promotion = invocation.getArgument(0);
                return PromotionEntity.builder()
                        .id(1L)
                        .description(promotion.getDescription())
                        .discountType(promotion.getDiscountType())
                        .discountRate(promotion.getDiscountRate())
                        .discountQuantity(promotion.getDiscountQuantity())
                        .isDiscountDuplicatable(promotion.getIsDiscountDuplicatable())
                        .startDate(promotion.getStartDate())
                        .endDate(promotion.getEndDate())
                        .build();
            });

            // when
            assertThatCode(() -> promotionService.register(request))
                    .doesNotThrowAnyException();

            // then
            verify(promotionRepository, times(1)).save(any(PromotionEntity.class));
            verify(promotionCacheService, times(1)).updatePromotionInCache(promotionEntityCaptor.capture());
        }
    }
}