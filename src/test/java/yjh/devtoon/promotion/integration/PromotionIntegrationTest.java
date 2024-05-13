package yjh.devtoon.promotion.integration;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import java.time.LocalDateTime;
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

    @Nested
    @DisplayName("프로모션 등록 테스트")
    class PromotionRegisterTests {

        private static final String description = "스릴러 프로모션입니다.";
        private static final LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 0, 0, 0);
        private static final LocalDateTime endDate = LocalDateTime.of(2024, 8, 15, 11, 59, 59);
        private static final String attributeName = "target-genre";
        private static final String attributeValue = "스릴러";

        @DisplayName("프로모션 등록 성공")
        @Test
        void registerPromotion_successfully() throws Exception {

            // given
            PromotionCreateRequest request = new PromotionCreateRequest(
                    description,
                    startDate,
                    endDate,
                    attributeName,
                    attributeValue
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
                LocalDateTime startDate,
                LocalDateTime endDate,
                String attributeName,
                String attributeValue
        ) throws Exception {

            // given
            PromotionCreateRequest request = new PromotionCreateRequest(
                    description,
                    startDate,
                    endDate,
                    attributeName,
                    attributeValue
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
                    Arguments.of(null, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                            "attributeName", "attributeValue"),
                    Arguments.of("", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                            "attributeName", "attributeValue"),
                    Arguments.of(" ", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                            "attributeName", "attributeValue"),
                    // startDate 유효성 검사
                    Arguments.of("description", null, LocalDateTime.now().plusDays(1),
                            "attributeName", "attributeValue"),
                    Arguments.of("description", LocalDateTime.now().minusDays(1),
                            LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    // endDate 유효성 검사
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().minusDays(1), "attributeName", "attributeValue"),
                    // attributeName 유효성 검사
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), null, "attributeValue"),
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), "", "attributeValue"),
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), " ", "attributeValue"),
                    // attributeValue 유효성 검사
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), "attributeName", null),
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), "attributeName", ""),
                    Arguments.of("description", LocalDateTime.now(),
                            LocalDateTime.now().plusDays(1), "attributeName", " ")
            );

        }

    }

    @Nested
    @DisplayName("프로모션 삭제 테스트")
    class PromotionSoftDeleteTests {

        private static final String description = "7월 프로모션입니다.";
        private static final LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 0, 0, 0);
        private static final LocalDateTime endDate = LocalDateTime.of(2024, 7, 31, 11, 59, 59);
        private static final String attributeName = "target-month";
        private static final String attributeValue = "7";

        @DisplayName("프로모션 삭제 성공")
        @Test
        void softDeletePromotion_successfully() throws Exception {

            // given
            PromotionEntity promotion = PromotionEntity.create(
                    description,
                    startDate,
                    endDate
            );
            PromotionEntity savedPromotion = promotionRepository.save(promotion);

            PromotionAttributeEntity attribute = PromotionAttributeEntity.create(
                    savedPromotion,
                    attributeName,
                    attributeValue
            );
            promotionAttributeRepository.save(attribute);

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

    @Nested
    @DisplayName("프로모션 조회 테스트")
    class RetrieveActivePromotionsTests {

        @DisplayName("현재 시간 기준 유효한 프로모션 전체 조회")
        @TestFactory
        Stream<DynamicTest> whenCurrentTimeGiven_thenListActivePromotionsPaged() {
            return Stream.of(
                    DynamicTest.dynamicTest("1. 프로모션1 저장 ->", () -> {
                        // given
                        PromotionEntity promotionEntity = PromotionEntity.builder()
                                .description("여름 프로모션")
                                .startDate(LocalDateTime.of(2024, 5, 13, 23, 44, 0))
                                .endDate(LocalDateTime.of(2024, 7, 1, 0, 0, 0))
                                .build();
                        PromotionEntity savedPromotion1 = promotionRepository.save(promotionEntity);
                        assertNotNull(savedPromotion1.getId());

                        PromotionAttributeEntity promotionAttributeEntity =
                                PromotionAttributeEntity.builder()
                                .promotionEntity(savedPromotion1)
                                .attributeName("target-month")
                                .attributeValue("6")
                                .build();
                        promotionAttributeRepository.save(promotionAttributeEntity);

                    }), DynamicTest.dynamicTest("2. 프로모션2 저장 ->", () -> {
                        // given
                        PromotionEntity promotionEntity = PromotionEntity.builder()
                                .description("스릴러 프로모션")
                                .startDate(LocalDateTime.of(2024, 5, 13, 23, 44, 0))
                                .endDate(LocalDateTime.of(2024, 7, 1, 0, 0, 0))
                                .build();
                        PromotionEntity savedPromotion2 = promotionRepository.save(promotionEntity);
                        assertNotNull(savedPromotion2.getId());

                        PromotionAttributeEntity promotionAttributeEntity =
                                PromotionAttributeEntity.builder()
                                .promotionEntity(savedPromotion2)
                                .attributeName("target-genre")
                                .attributeValue("thriller")
                                .build();
                        promotionAttributeRepository.save(promotionAttributeEntity);

                    }), DynamicTest.dynamicTest("3. 유효한 프로모션 조회", () -> {
                        // when, then
                        mockMvc.perform(get("/v1/promotions/now")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusMessage").value("성공"))
                                .andExpect(jsonPath("$.data.content[0].description").value("여름 " +
                                        "프로모션"))
                                .andExpect(jsonPath("$.data.content[0].startDate").value("2024-05" +
                                        "-13T23:44:00"))
                                .andExpect(jsonPath("$.data.content[0].endDate").value("2024-07" +
                                        "-01T00:00:00"))
                                .andExpect(jsonPath("$.data.content[0].attributeName").value(
                                        "target-month"))
                                .andExpect(jsonPath("$.data.content[0].attributeValue").value("6"))
                                .andExpect(jsonPath("$.data.content[1].description").value("스릴러 " +
                                        "프로모션"))
                                .andExpect(jsonPath("$.data.content[1].startDate").value("2024-05" +
                                        "-13T23:44:00"))
                                .andExpect(jsonPath("$.data.content[1].endDate").value("2024-07" +
                                        "-01T00:00:00"))
                                .andExpect(jsonPath("$.data.content[1].attributeName").value(
                                        "target-genre"))
                                .andExpect(jsonPath("$.data.content[1].attributeValue").value(
                                        "thriller"));
                    })
            );
        }

        @DisplayName("현재 시간 기준 유효한 프로모션 조회 - 등록된 프로모션이 현재 시간에는 유효하지 않을 경우 빈 페이지 반환")
        @TestFactory
        Stream<DynamicTest> whenCurrentTimeGiven_AndNoPromotions_thenReturnEmptyPage() throws Exception {
            return Stream.of(
                    DynamicTest.dynamicTest("1. 과거 프로모션1 저장 ->", () -> {
                        // given
                        PromotionEntity promotionEntity = PromotionEntity.builder()
                                .description("여름 프로모션")
                                .startDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                                .endDate(LocalDateTime.of(2024, 5, 1, 0, 0, 0))
                                .build();
                        PromotionEntity savedPromotion1 = promotionRepository.save(promotionEntity);

                        PromotionAttributeEntity promotionAttributeEntity =
                                PromotionAttributeEntity.builder()
                                .promotionEntity(savedPromotion1)
                                .attributeName("target-month")
                                .attributeValue("6")
                                .build();
                        promotionAttributeRepository.save(promotionAttributeEntity);

                    }), DynamicTest.dynamicTest("2. 과거 프로모션2 저장 ->", () -> {
                        // given
                        PromotionEntity promotionEntity = PromotionEntity.builder()
                                .description("스릴러 프로모션")
                                .startDate(LocalDateTime.of(2024, 2, 1, 0, 0, 0))
                                .endDate(LocalDateTime.of(2024, 5, 1, 0, 0, 0))
                                .build();
                        PromotionEntity savedPromotion2 = promotionRepository.save(promotionEntity);

                        PromotionAttributeEntity promotionAttributeEntity =
                                PromotionAttributeEntity.builder()
                                .promotionEntity(savedPromotion2)
                                .attributeName("target-genre")
                                .attributeValue("thriller")
                                .build();
                        promotionAttributeRepository.save(promotionAttributeEntity);

                    }), DynamicTest.dynamicTest("3. 유효한 프로모션 조회", () -> {
                        // when, then
                        mockMvc.perform(get("/v1/promotions/now")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusMessage").value("성공"))
                                .andExpect(jsonPath("$.data.content").isEmpty());
                    })
            );
        }

    }

}
