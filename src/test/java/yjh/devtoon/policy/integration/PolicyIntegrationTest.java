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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import java.util.HashMap;
import java.util.Map;

@DisplayName("통합 테스트 [Policy]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PolicyIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("정책 등록 테스트")
    class PolicyRegisterTests {

        private String type = null;
        private final Map<String, Object> details = new HashMap<>();

        @DisplayName("정책 등록 성공 (cookie_policy)")
        @Test
        void registerCookiePolicy_successfully() throws Exception {

            type = "cookie";
            details.put("cookiePrice", 300);
            details.put("cookieQuantityPerEpisode", 2);
            details.put("startDate", "2024-05-02T00:00");
            details.put("endDate", "2024-12-31T23:59:59");

            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    type,
                    details
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").isNumber());

        }

        @DisplayName("정책 등록 성공 (bad_words_policy)")
        @Test
        void registerBadWordsPolicy_successfully() throws Exception {

            type = "bad_words";
            details.put("warningThreshold", 3);
            details.put("startDate", "2024-05-05T00:00");
            details.put("endDate", "2024-12-31T23:59:59");

            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    type,
                    details
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data").isNumber());

        }

        @DisplayName("정책 등록 실패 - 정의되지 않은 정책 타입으로 등록 요청한 경우")
        @Test
        void whenRegisteringUndefinedPolicyType_thenFail() throws Exception {

            type = "VIP";
            details.put("memberGrade", 3);
            details.put("startDate", "2024-05-05T00:00");
            details.put("endDate", "2024-12-31T23:59:59");

            // given
            PolicyCreateRequest request = new PolicyCreateRequest(
                    type,
                    details
            );
            String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(post("/v1/policies")
                            .content(requestBody)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.message").value("요청사항을 찾지 못했습니다."));

        }

    }

}
