package yjh.devtoon.policy.integration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@DisplayName("통합 테스트 [Policy]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PolicyIntegrationTest {

    private static final String NULL = null;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "email@gmail.com", password = "password", authorities = {"ADMIN"})
    @Nested
    @DisplayName("정책 등록 테스트")
    class PolicyRegisterTests {

        private static final String COOKIE_POLICY_NAME = "COOKIE_POLICY";
        private static final String BAD_WORDS_POLICY_NAME = "BAD_WORDS_POLICY";
        private static final LocalDateTime START_DATE = LocalDateTime.parse("2024-05-02T00:00");
        private static final LocalDateTime END_DATE = LocalDateTime.parse("2024-12-31T23:59:59");
        private static final BigDecimal COOKIE_PRICE = BigDecimal.valueOf(200);
        private static final int COOKIE_QUANTITY_PER_EPISODE = 3;
        private static final int WARNING_THRESHOLD = 3;

        @DisplayName("쿠키 정책 등록 성공")
        @Test
        void registerCookiePolicy_successfully() throws Exception {
            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    COOKIE_POLICY_NAME,
                    START_DATE,
                    END_DATE,
                    COOKIE_PRICE,
                    COOKIE_QUANTITY_PER_EPISODE
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("비속어 정책 등록 성공")
        @Test
        void registerBadWordsPolicy_successfully() throws Exception {
            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    BAD_WORDS_POLICY_NAME,
                    START_DATE,
                    END_DATE,
                    WARNING_THRESHOLD
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").value(NULL));
        }

        @DisplayName("정책 등록 실패 - 정의되지 않은 정책 타입으로 등록 요청한 경우")
        @Test
        void whenRegisteringUndefinedPolicyType_thenFail() throws Exception {
            String VIP_POLICY_NAME = "VIP_COOKIE_POLICY";

            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    VIP_POLICY_NAME,
                    START_DATE,
                    END_DATE,
                    WARNING_THRESHOLD
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"));
        }
    }
}


