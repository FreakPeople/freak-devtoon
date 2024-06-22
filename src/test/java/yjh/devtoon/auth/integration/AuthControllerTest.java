package yjh.devtoon.auth.integration;

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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.auth.dto.request.LoginRequest;
import yjh.devtoon.member.domain.Authority;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.Role;
import yjh.devtoon.member.infrastructure.MemberRepository;
import java.util.Set;

@DisplayName("통합 테스트 [Auth]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("로그인(인증) 기능 테스트")
    class LoginTests {

        private static final String NULL = null;
        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("로그인(인증) 성공")
        @Test
        void login_successfully() throws Exception {
            // given
            memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(passwordEncoder.encode(VALID_FILED_PASSWORD))
                    .membershipStatus(MembershipStatus.GENERAL)
                    .authorities(Set.of(new Authority(Role.MEMBER)))
                    .build()
            );

            final LoginRequest request = new LoginRequest(
                    VALID_FILED_EMAIL,
                    VALID_FILED_PASSWORD
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/auth/authenticate")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
        }

        @DisplayName("로그인(인증) 실패")
        @Test
        void givenInvalidLoginRequest_whenLogin_thenFalure() throws Exception {
            // given
            memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(passwordEncoder.encode(VALID_FILED_PASSWORD))
                    .membershipStatus(MembershipStatus.GENERAL)
                    .authorities(Set.of(new Authority(Role.MEMBER)))
                    .build()
            );

            final LoginRequest request = new LoginRequest(
                    "invalid_email",
                    "invalid_password"
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/auth/authenticate")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"));
        }

    }

}