package yjh.devtoon.promotion.application;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionAttributeRepository promotionAttributeRepository;

    @Nested
    @DisplayName("프로모션 등록 테스트")
    class RegisterPromotionTests {

        @Test
        @DisplayName("프로모션 등록 요청 시 성공적으로 등록")
        void registerPromotion_successfully() {

            // given
            String description = "12월 크리스마스 프로모션입니다.";
            LocalDateTime startDate = LocalDateTime.parse("2024-05-10T00:00:00");
            LocalDateTime endDate = LocalDateTime.parse("2024-05-13T11:59:59");
            String attributeName = "target-month";
            String attributeValue = "12";

            PromotionCreateRequest request = new PromotionCreateRequest(
                    description, startDate, endDate, attributeName, attributeValue
            );

            // when, then
            assertThatCode(() -> promotionService.register(request))
                    .doesNotThrowAnyException();
        }

    }

}