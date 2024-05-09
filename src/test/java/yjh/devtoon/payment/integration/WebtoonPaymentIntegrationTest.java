package yjh.devtoon.payment.integration;

import static org.assertj.core.api.Assertions.assertThat;
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
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.payment.dto.request.WebtoonPaymentCreateRequest;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@DisplayName("통합 테스트 [WebtoonPaymentIntegration]")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebtoonPaymentIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebtoonViewerRepository webtoonViewerRepository;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private CookiePolicyRepository cookiePolicyRepository;

    @Autowired
    private CookieWalletRepository cookieWalletRepository;

    @Nested
    @DisplayName("웹툰 미리보기 결제 테스트")
    class WebtoonPaymentTests {

        @DisplayName("웹툰 미리보기 결제 등록 성공")
        @Test
        void registerWebtoonPayment_successfully() throws Exception {

            // given
            // 1. webtoon_viewer 등록
            WebtoonViewerEntity savedWebtoonViewer = webtoonViewerRepository.save(WebtoonViewerEntity.builder()
                    .name("홍길동")
                    .email("email@gmail.ocm")
                    .password("password")
                    .membershipStatus(MembershipStatus.GENERAL)
                    .build()
            );

            // 2. webtoon 등록
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .id(1L)
                    .title("기기괴괴")
                    .writerName("오성대")
                    .build());

            // 3. cookie_policy 등록
            CookiePolicyEntity savedCookiePolicy = cookiePolicyRepository.save(CookiePolicyEntity.builder()
                    .cookiePrice(BigDecimal.valueOf(200))
                    .cookieQuantityPerEpisode(3)
                    .startDate(LocalDateTime.parse("2024-05-02T00:00"))
                    .build());

            // 4. cookieWallet 등록
            CookieWalletEntity savedCookieWallet = cookieWalletRepository.save(CookieWalletEntity.builder()
                    .webtoonViewerId(savedWebtoonViewer.getId())
                    .quantity(5)
                    .build());

            final WebtoonPaymentCreateRequest request = new WebtoonPaymentCreateRequest(
                    savedWebtoonViewer.getId(),
                    savedWebtoon.getId(),
                    3L
            );
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            mockMvc.perform(post("/v1/webtoon-payments")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"));

            // then
            CookieWalletEntity cookieWallet = cookieWalletRepository.findById(savedWebtoonViewer.getId()).get();
            assertThat(cookieWallet.getQuantity()).isEqualTo(2);

        }

    }

}
