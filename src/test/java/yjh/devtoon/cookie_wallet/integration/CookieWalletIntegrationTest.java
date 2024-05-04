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
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;

@DisplayName("통합 테스트 [CookieWalletIntegration]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CookieWalletIntegrationTest {

    @Autowired
    private CookieWalletRepository cookieWalletRepository;

    @Autowired
    private WebtoonViewerRepository webtoonViewerRepository;

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
            WebtoonViewerEntity savedViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    savedViewer.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            // when
            mockMvc.perform(get("/v1/cookie-wallets")
                            .param("webtoonViewerNo", String.valueOf(savedViewer.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonViewerNo").value(savedViewer.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("10"));
        }

        @DisplayName("쿠키 지갑 조회 실패 - 해당 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenRetrieveCookieWallet_thenThrowException() throws Exception {
            // given
            final long notExistWebtoonViewerId = 999999999999999L;

            // when
            mockMvc.perform(get("/v1/cookie-wallets")
                            .param("webtoonViewerNo", String.valueOf(notExistWebtoonViewerId))
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
            WebtoonViewerEntity savedViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    savedViewer.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/increase")
                            .param("webtoonViewerNo", String.valueOf(savedViewer.getId()))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonViewerNo").value(savedViewer.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("15"));
        }

        @DisplayName("쿠키 증가 실패 - 대상 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenIncreaseCookie_thenThrowException() throws Exception {
            // given
            final long notExistWebtoonViewerId = 999999999999999L;
            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/increase")
                            .param("webtoonViewerNo", String.valueOf(notExistWebtoonViewerId))
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
            WebtoonViewerEntity savedViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name(VALID_FILED_TITLE)
                    .email(VALID_FILED_EMAIL)
                    .password(VALID_FILED_PASSWORD)
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );
            CookieWalletEntity cookieWallet = new CookieWalletEntity(
                    savedViewer.getId(),
                    10,
                    null
            );
            cookieWalletRepository.save(cookieWallet);

            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/decrease")
                            .param("webtoonViewerNo", String.valueOf(savedViewer.getId()))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.webtoonViewerNo").value(savedViewer.getId()))
                    .andExpect(jsonPath("$.data.quantity").value("5"));
        }

        @DisplayName("쿠키 감소 실패 - 대상 id의 회원이 존재 하지 않음")
        @Test
        void givenNotExistWebtoonViewerId_whenDecreaseCookie_thenThrowException() throws Exception {
            // given
            final long notExistWebtoonViewerId = 999999999999999L;
            String requestBody = "{\"quantity\": 5}";

            // when
            mockMvc.perform(put("/v1/cookie-wallets/decrease")
                            .param("webtoonViewerNo", String.valueOf(notExistWebtoonViewerId))
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("실패"))
                    .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()));
        }

    }

}