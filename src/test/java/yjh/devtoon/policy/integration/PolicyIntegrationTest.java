package yjh.devtoon.policy.integration;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.dto.request.PolicyCreateRequest;
import yjh.devtoon.policy.infrastructure.BadWordsPolicyRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private CookiePolicyRepository cookiePolicyRepository;

    @Autowired
    private BadWordsPolicyRepository badWordsPolicyRepository;

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

    @Nested
    @DisplayName("활성 정책 조회 테스트")
    class RetrieveActivePoliciesTests {

        @DisplayName("활성 정책 조회 성공 - 활성 정책이 존재할 경우, 모든 활성 정책을 리스트로 반환")
        @Test
        void whenPoliciesAreActive_thenReturnListOfActivePolicies() throws Exception {

            // given
            Map<String, Object> cookieDetails = new HashMap<>();
            cookieDetails.put("type", "cookie");
            cookieDetails.put("cookiePrice", new BigDecimal("300"));
            cookieDetails.put("cookieQuantityPerEpisode", 3);
            cookieDetails.put("startDate", LocalDateTime.parse("2024-01-01T00:00"));
            cookieDetails.put("endDate", LocalDateTime.parse("2024-12-31T23:59:59"));

            CookiePolicyEntity cookiePolicyEntity = new CookiePolicyEntity(cookieDetails);
            CookiePolicyEntity savedCookiePolicyEntity = cookiePolicyRepository.save(cookiePolicyEntity);
            assertNotNull(savedCookiePolicyEntity.getId());

            Map<String, Object> badWordsDetails = new HashMap<>();
            cookieDetails.put("type", "bad_words");
            badWordsDetails.put("warningThreshold", 5);
            badWordsDetails.put("startDate", LocalDateTime.parse("2024-05-01T00:00"));
            badWordsDetails.put("endDate", LocalDateTime.parse("2024-12-31T23:59:59"));

            BadWordsPolicyEntity badWordsPolicyEntity = new BadWordsPolicyEntity(badWordsDetails);
            BadWordsPolicyEntity savedBadWordsPolicyEntity = badWordsPolicyRepository.save(badWordsPolicyEntity);
            assertNotNull(savedBadWordsPolicyEntity.getId());

            // when, then
            mockMvc.perform(get("/v1/policies/active")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.policyDetailsInfo[0].id").value(savedCookiePolicyEntity.getId()))
                    .andExpect(jsonPath("$.data.policyDetailsInfo[0].type").value("CookiePolicyEntity"))
                    .andExpect(jsonPath("$.data.policyDetailsInfo[1].id").value(savedBadWordsPolicyEntity.getId()))
                    .andExpect(jsonPath("$.data.policyDetailsInfo[1].type").value("BadWordsPolicyEntity"));

        }

        @DisplayName("활성 정책 조회 성공 - 현재 활성화된 정책이 없을 경우, 빈 리스트 반환")
        @Test
        void whenNoActivePoliciesExist_thenReturnEmptyList() throws Exception {

            // given
            Map<String, Object> cookieDetails = new HashMap<>();
            cookieDetails.put("type", "cookie");
            cookieDetails.put("cookiePrice", new BigDecimal("500"));
            cookieDetails.put("cookieQuantityPerEpisode", 5);
            cookieDetails.put("startDate", LocalDateTime.parse("2024-10-01T00:00"));
            cookieDetails.put("endDate", LocalDateTime.parse("2024-12-31T23:59:59"));

            CookiePolicyEntity cookiePolicyEntity = new CookiePolicyEntity(cookieDetails);
            cookiePolicyRepository.save(cookiePolicyEntity);

            Map<String, Object> badWordsDetails = new HashMap<>();
            cookieDetails.put("type", "bad_words");
            badWordsDetails.put("warningThreshold", 10);
            badWordsDetails.put("startDate", LocalDateTime.parse("2024-12-01T00:00"));
            badWordsDetails.put("endDate", LocalDateTime.parse("2024-12-31T23:59:59"));

            BadWordsPolicyEntity badWordsPolicyEntity = new BadWordsPolicyEntity(badWordsDetails);
            badWordsPolicyRepository.save(badWordsPolicyEntity);

            // when, then
            mockMvc.perform(get("/v1/policies/active")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.policyDetailsInfo").isEmpty());

        }

    }

}
