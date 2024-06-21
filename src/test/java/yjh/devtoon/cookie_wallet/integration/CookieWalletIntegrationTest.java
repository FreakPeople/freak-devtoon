package yjh.devtoon.cookie_wallet.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.infrastructure.MemberRepository;

@DisplayName("통합 테스트 [CookieWalletIntegration]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CookieWalletIntegrationTest {

    @Autowired
    private CookieWalletRepository cookieWalletRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("쿠키 지갑 조회 기능 테스트")
    class CookieWalletRetrieveTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("쿠키 지갑 조회 성공")
        @Test
        void retrieveCookieWallet_successfully() throws Exception {
            // given
            MemberEntity member = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    member.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            // when
            mockMvc.perform(get("/v1/cookie-wallets")
                            .param("memberId", String.valueOf(member.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("10"));
        }

        @DisplayName("쿠키 지갑 조회 실패 - 해당 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistMemberId_whenRetrieveCookieWallet_thenThrowException() throws Exception {
            // given
            final long notExistMemberId = 999999999999999L;

            // when
            mockMvc.perform(get("/v1/cookie-wallets")
                            .param("memberId", String.valueOf(notExistMemberId))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @Nested
    @DisplayName("쿠기 증가 기능 테스트")
    class CookieIncreaseTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("쿠키 증가 성공")
        @Test
        void increaseCookie_successfully() throws Exception {
            // given
            MemberEntity member = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    member.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/increase")
                            .param("memberId", String.valueOf(member.getId()))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("15"));
        }

        @DisplayName("쿠키 증가 실패 - 대상 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistMemberId_whenIncreaseCookie_thenThrowException() throws Exception {
            // given
            final long notExistMemberId = 999999999999999L;
            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/increase")
                            .param("memberId", String.valueOf(notExistMemberId))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

    @Nested
    @DisplayName("쿠기 감소 기능 테스트")
    class CookieDecreaseTests {

        private static final String VALID_FILED_TITLE = "홍길동";
        private static final String VALID_FILED_EMAIL = "email@gmail.ocm";
        private static final String VALID_FILED_PASSWORD = "password";

        @DisplayName("쿠키 감소 성공")
        @Test
        void decreaseCookie_successfully() throws Exception {
            // given
            MemberEntity member = memberRepository.save(MemberEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    member.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/decrease")
                            .param("memberId", String.valueOf(member.getId()))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("5"));
        }

        @DisplayName("쿠키 감소 실패 - 대상 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistMemberId_whenDecreaseCookie_thenThrowException() throws Exception {
            // given
            final long notExistMemberId = 999999999999999L;
            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/decrease")
                            .param("memberId", String.valueOf(notExistMemberId))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

}