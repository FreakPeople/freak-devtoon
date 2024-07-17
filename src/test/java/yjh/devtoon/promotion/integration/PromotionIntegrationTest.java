package yjh.devtoon.promotion.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yjh.devtoon.promotion.domain.DiscountType.CASH_DISCOUNT;
import static yjh.devtoon.promotion.domain.DiscountType.COOKIE_QUANTITY_DISCOUNT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.promotion.domain.DiscountType;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionAttributeCreateRequest;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("통합 테스트 [Promotion]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PromotionIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionAttributeRepository promotionAttributeRepository;

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("프로모션 등록 테스트")
    class PromotionRegisterTests {

        private static final String DESCRIPTION = "7월 스릴러 장르 파격 할인 행사입니다.";
        private static final DiscountType DISCOUNT_TYPE = COOKIE_QUANTITY_DISCOUNT;
        private static final BigDecimal DISCOUNT_RATE = null;
        private static final Integer DISCOUNT_QUANTITY = 1;
        private static final Boolean IS_DISCOUNT_DUPLICATABLE = true;
        private static final LocalDateTime STARTDATE = LocalDateTime.now().plusDays(1);
        private static final LocalDateTime ENDDATE = LocalDateTime.now().plusDays(30);
        private static final List<PromotionAttributeCreateRequest> PROMOTION_ATTRIBUTES = List.of(
                new PromotionAttributeCreateRequest("target-month", "7"),
                new PromotionAttributeCreateRequest("target-genre", "thriller")
        );

        @DisplayName("프로모션 등록 성공")
        @Test
        void registerPromotion_successfully() throws Exception {
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
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/promotions")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));

        }

        @DisplayName("프로모션 등록 실패 - 필드 유효성 검사")
        @ParameterizedTest
        @MethodSource("givenInvalidField_whenRegisterPromotion_thenThrowException")
        void givenInvalidField_whenRegisterPromotion_thenThrowException(
                String description,
                DiscountType discountType,
                Boolean isDiscountDuplicatable,
                LocalDateTime startDate,
                List<PromotionAttributeCreateRequest> promotionAttributes
        ) throws Exception {

            // given
            PromotionCreateRequest request = new PromotionCreateRequest(
                    description,
                    discountType,
                    DISCOUNT_RATE,
                    DISCOUNT_QUANTITY,
                    isDiscountDuplicatable,
                    startDate,
                    ENDDATE,
                    promotionAttributes
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/promotions")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.BAD_REQUEST.value()));

        }

        private static Stream<Arguments> givenInvalidField_whenRegisterPromotion_thenThrowException() {
            return Stream.of(
                    // description 유효성 검사
                    Arguments.of(null, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            PROMOTION_ATTRIBUTES),
                    Arguments.of("", DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            PROMOTION_ATTRIBUTES),
                    Arguments.of(" ", DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            PROMOTION_ATTRIBUTES),
                    // startDate 유효성 검사
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, null,
                            PROMOTION_ATTRIBUTES),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE,
                            LocalDateTime.now().minusDays(1), PROMOTION_ATTRIBUTES),
                    // promotionAttributes 유효성 검사
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest(null, "value"))),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest("", "value"))),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest(" ", "value"))),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest("name", null))),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest("name", ""))),
                    Arguments.of(DESCRIPTION, DISCOUNT_TYPE, IS_DISCOUNT_DUPLICATABLE, STARTDATE,
                            List.of(
                                    new PromotionAttributeCreateRequest("name", " ")))
            );

        }

    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("프로모션 삭제 테스트")
    class PromotionSoftDeleteTests {

        private static final String DESCRIPTION = "12월 로맨스 장르 파격 할인 행사입니다.";
        private static final DiscountType DISCOUNT_TYPE = CASH_DISCOUNT;
        private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(10);
        private static final Integer DISCOUNT_QUANTITY = null;
        private static final Boolean IS_DISCOUNT_DUPLICATABLE = true;
        private static final LocalDateTime STARTDATE = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
        private static final LocalDateTime ENDDATE = LocalDateTime.of(2024, 12, 31, 11, 59, 59);
        private static final List<PromotionAttributeCreateRequest> PROMOTION_ATTRIBUTES = List.of(
                new PromotionAttributeCreateRequest("target-month", "12"),
                new PromotionAttributeCreateRequest("target-genre", "romance")
        );

        @DisplayName("프로모션 삭제 성공")
        @Test
        void softDeletePromotion_successfully() throws Exception {
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

            PromotionEntity promotion = PromotionEntity.create(
                    DESCRIPTION,
                    DISCOUNT_TYPE,
                    DISCOUNT_RATE,
                    DISCOUNT_QUANTITY,
                    IS_DISCOUNT_DUPLICATABLE,
                    STARTDATE,
                    ENDDATE
            );
            PromotionEntity savedPromotion = promotionRepository.save(promotion);

            PROMOTION_ATTRIBUTES.stream()
                    .map(promotionAttributeRequest -> PromotionAttributeEntity.create(
                            savedPromotion,
                            promotionAttributeRequest.getAttributeName(),
                            promotionAttributeRequest.getAttributeValue()
                    ))
                    .forEach(promotionAttributeRepository::save);

            // when, then
            mockMvc.perform(delete("/v1/promotions/" + savedPromotion.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.promotionId").value(savedPromotion.getId()));

        }

        @DisplayName("프로모션 삭제 실패 - 존재하지 않는 프로모션 id로 삭제한 경우")
        @Test
        void givenNotExistPromotionId_whenSoftDeletePromotion_thenThrowException() throws Exception {

            // given
            Long nonExistPromotionId = 99999999999999L;

            // when, then
            mockMvc.perform(delete("/v1/promotions/{id}", nonExistPromotionId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));

        }

    }

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"MEMBER"})
    @Nested
    @DisplayName("프로모션 조회 테스트")
    class RetrieveActivePromotionsTests {

        private static final List<PromotionAttributeCreateRequest> PROMOTION_ATTRIBUTES = List.of(
                new PromotionAttributeCreateRequest("target-month", "12"),
                new PromotionAttributeCreateRequest("target-genre", "romance")
        );

        @DisplayName("현재 적용 가능한 모든 프로모션 조회")
        @TestFactory
        Stream<DynamicTest> whenCurrentTimeGiven_thenReturnActivePromotionsList() {
            return Stream.of(
                    DynamicTest.dynamicTest("1. 프로모션1 저장 ->", () -> {
                        // given
                        PromotionEntity promotion1 = PromotionEntity.create(
                                "12월 로맨스 장르 파격 할인 행사입니다.",
                                CASH_DISCOUNT,
                                BigDecimal.valueOf(20),
                                null,
                                true,
                                LocalDateTime.now().minusMonths(1),
                                LocalDateTime.now().plusMonths(1)
                        );
                        PromotionEntity savedPromotion1 = promotionRepository.save(promotion1);

                        PROMOTION_ATTRIBUTES.stream()
                                .map(promotionAttributeRequest -> PromotionAttributeEntity.create(
                                        savedPromotion1,
                                        promotionAttributeRequest.getAttributeName(),
                                        promotionAttributeRequest.getAttributeValue()
                                ))
                                .forEach(promotionAttributeRepository::save);

                    }), DynamicTest.dynamicTest("2. 프로모션2 저장 ->", () -> {
                        // given
                        PromotionEntity promotion2 = PromotionEntity.create(
                                "7월 로맨스 장르 파격 할인 행사입니다.",
                                COOKIE_QUANTITY_DISCOUNT,
                                null,
                                2,
                                true,
                                LocalDateTime.now().minusMonths(1),
                                LocalDateTime.now().plusMonths(1)
                        );
                        PromotionEntity savedPromotion2 = promotionRepository.save(promotion2);

                        PROMOTION_ATTRIBUTES.stream()
                                .map(promotionAttributeRequest -> PromotionAttributeEntity.create(
                                        savedPromotion2,
                                        promotionAttributeRequest.getAttributeName(),
                                        promotionAttributeRequest.getAttributeValue()
                                ))
                                .forEach(promotionAttributeRepository::save);

                    }), DynamicTest.dynamicTest("3. 현재 적용 가능한 모든 프로모션 조회", () -> {

                        // when, then
                        mockMvc.perform(get("/v1/promotions/now")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusMessage").value("성공"));
                        // .andExpect(jsonPath("$.data[0].description").value("12월 로맨스 장르 " +
                        // "파격 할인 행사입니다."));
//                                .andExpect(jsonPath("$.data[1].description").value("7월 로맨스 장르
//                                파격 " +
//                                        "할인 행사입니다."));

                    })
            );
        }

        @DisplayName("현재 적용 가능한 모든 프로모션 조회 - 해당 프로모션이 없는 경우 빈 리스트를 반환")
        @TestFactory
        Stream<DynamicTest> whenCurrentTimeGiven_AndNoPromotions_thenReturnEmptyList() throws
                Exception {
            return Stream.of(
                    DynamicTest.dynamicTest("1. 과거 프로모션1 저장 ->", () -> {
                        // given
                        PromotionEntity promotion1 = PromotionEntity.create(
                                "12월 로맨스 장르 파격 할인 행사입니다.",
                                CASH_DISCOUNT,
                                BigDecimal.valueOf(20),
                                null,
                                true,
                                LocalDateTime.now().minusMonths(2),
                                LocalDateTime.now().minusMonths(1)
                        );
                        PromotionEntity savedPromotion1 = promotionRepository.save(promotion1);

                        PROMOTION_ATTRIBUTES.stream()
                                .map(promotionAttributeRequest -> PromotionAttributeEntity.create(
                                        savedPromotion1,
                                        promotionAttributeRequest.getAttributeName(),
                                        promotionAttributeRequest.getAttributeValue()
                                ))
                                .forEach(promotionAttributeRepository::save);

                    }), DynamicTest.dynamicTest("2. 과거 프로모션2 저장 ->", () -> {
                        // given
                        PromotionEntity promotion2 = PromotionEntity.create(
                                "7월 로맨스 장르 파격 할인 행사입니다.",
                                COOKIE_QUANTITY_DISCOUNT,
                                null,
                                2,
                                true,
                                LocalDateTime.now().minusMonths(2),
                                LocalDateTime.now().minusMonths(1)
                        );
                        PromotionEntity savedPromotion2 = promotionRepository.save(promotion2);

                        PROMOTION_ATTRIBUTES.stream()
                                .map(promotionAttributeRequest -> PromotionAttributeEntity.create(
                                        savedPromotion2,
                                        promotionAttributeRequest.getAttributeName(),
                                        promotionAttributeRequest.getAttributeValue()
                                ))
                                .forEach(promotionAttributeRepository::save);

                    }), DynamicTest.dynamicTest("3. 유효한 프로모션 조회", () -> {
                        // when, then
                        mockMvc.perform(get("/v1/promotions/now")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusMessage").value("성공"));
                        //.andExpect(jsonPath("$.data[*]").isEmpty());
                    })
            );
        }

        @DisplayName("종료된 모든 프로모션 조회")
        @Test
        void retrieveAllEndedPromotions_successfully() throws Exception {
            // given
            PromotionEntity endedPromotion1 = PromotionEntity.builder()
                    .id(1L)
                    .description("1월 할인 프로모션")
                    .discountType(COOKIE_QUANTITY_DISCOUNT)
                    .discountQuantity(3)
                    .isDiscountDuplicatable(true)
                    .startDate(LocalDateTime.now().minusMonths(1))
                    .deletedAt(LocalDateTime.parse("2024-06-01T00:00:00"))
                    .build();
            promotionRepository.save(endedPromotion1);

            PromotionEntity endedPromotion2 = PromotionEntity.builder()
                    .id(2L)
                    .description("2월 할인 프로모션")
                    .discountType(CASH_DISCOUNT)
                    .discountRate(BigDecimal.valueOf(30))
                    .isDiscountDuplicatable(true)
                    .startDate(LocalDateTime.now().minusMonths(2))
                    .deletedAt(LocalDateTime.parse("2024-06-02T00:00:00"))
                    .build();
            promotionRepository.save(endedPromotion2);

            // when, then
            mockMvc.perform(get("/v1/promotions/ended")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].description").value("1월 할인 프로모션"))
                    .andExpect(jsonPath("$.data[0].deletedAt").value("2024-06-01T00:00:00"))
                    .andExpect(jsonPath("$.data[1].description").value("2월 할인 프로모션"))
                    .andExpect(jsonPath("$.data[1].deletedAt").value("2024-06-02T00:00:00"));
        }

    }

}
