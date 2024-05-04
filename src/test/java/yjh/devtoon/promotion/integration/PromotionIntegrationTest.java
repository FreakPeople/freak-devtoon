package yjh.devtoon.promotion.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
                    Arguments.of(null, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    Arguments.of("", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    Arguments.of(" ", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    // startDate 유효성 검사
                    Arguments.of("description", null, LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    Arguments.of("description", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), "attributeName", "attributeValue"),
                    // endDate 유효성 검사
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().minusDays(1), "attributeName", "attributeValue"),
                    // attributeName 유효성 검사
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, "attributeValue"),
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "", "attributeValue"),
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), " ", "attributeValue"),
                    // attributeValue 유효성 검사
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", null),
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", ""),
                    Arguments.of("description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "attributeName", " ")
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

}
