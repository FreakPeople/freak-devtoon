package yjh.devtoon.payment.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yjh.devtoon.promotion.domain.DiscountType.COOKIE_QUANTITY_DISCOUNT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;
import yjh.devtoon.payment.dto.request.WebtoonPaymentCreateRequest;
import yjh.devtoon.payment.infrastructure.WebtoonPaymentRepository;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import yjh.devtoon.webtoon.domain.Genre;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.infrastructure.MemberRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@DisplayName("통합 테스트 [WebtoonPaymentIntegration]")
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebtoonPaymentIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private CookiePolicyRepository cookiePolicyRepository;

    @Autowired
    private CookieWalletRepository cookieWalletRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionAttributeRepository promotionAttributeRepository;

    @Autowired
    private WebtoonPaymentRepository webtoonPaymentRepository;

    @Nested
    @DisplayName("웹툰 미리보기 결제 테스트")
    class WebtoonPaymentTests {

        @DisplayName("웹툰 미리보기 결제 등록 성공")
        @Test
        void registerWebtoonPayment_successfully() throws Exception {
            // given
            // 1. member 등록
            MemberEntity savedMember =
                    memberRepository.save(MemberEntity.builder()
                            .name("홍길동")
                            .email("email@gmail.ocm")
                            .password("password")
                            .membershipStatus(MembershipStatus.PREMIUM)
                            .build()
                    );

            // 2. webtoon 등록
            WebtoonEntity savedWebtoon = webtoonRepository.save(WebtoonEntity.builder()
                    .id(1L)
                    .title("기기괴괴")
                    .writerName("오성대")
                    .genre(Genre.HORROR)
                    .build());

            // 3. cookie_policy 등록
            CookiePolicyEntity savedCookiePolicy =
                    cookiePolicyRepository.save(CookiePolicyEntity.builder()
                            .cookiePrice(BigDecimal.valueOf(200))
                            .cookieQuantityPerEpisode(3)
                            .startDate(LocalDateTime.parse("2024-05-02T00:00:00"))
                            .build());

            // 4. promotion 등록
            PromotionEntity savedPromotion = promotionRepository.save(PromotionEntity.builder()
                    .description("작가 프로모션입니다.")
                    .isDiscountDuplicatable(true)
                    .discountType(COOKIE_QUANTITY_DISCOUNT)
                    .discountQuantity(1)
                    .startDate(LocalDateTime.parse("2024-06-01T00:00:00"))
                    .build());

            // 4-1. promotion attributes 등록
            PromotionAttributeEntity savedPromotionAttribute1 =
                    promotionAttributeRepository.save(PromotionAttributeEntity.builder()
                            .promotionEntity(savedPromotion)
                            .attributeName("target_author")
                            .attributeValue("오성대")
                            .build());

            PromotionAttributeEntity savedPromotionAttribute2 =
                    promotionAttributeRepository.save(PromotionAttributeEntity.builder()
                            .promotionEntity(savedPromotion)
                            .attributeName("target_genre")
                            .attributeValue(Genre.HORROR.getName())
                            .build());

            // 5. cookieWallet 등록
            CookieWalletEntity savedCookieWallet =
                    cookieWalletRepository.save(CookieWalletEntity.builder()
                            .memberId(savedMember.getId())
                            .quantity(5)
                            .build());

            final WebtoonPaymentCreateRequest request = new WebtoonPaymentCreateRequest(
                    savedMember.getId(),
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
            CookieWalletEntity cookieWallet =
                    cookieWalletRepository.findById(savedMember.getId()).get();
            assertThat(cookieWallet.getQuantity()).isEqualTo(3);
        }

        @DisplayName("특정 회원 웹툰 결제 내역 단건 조회 성공")
        @Test
        void retrieveWebtoonPayment_forSpecificMember_successfully() throws Exception {

            // given
            // 1. member 등록
            MemberEntity member =
                    memberRepository.save(MemberEntity.builder()
                            .name("둘리")
                            .email("email@gmail.ocm")
                            .password("password")
                            .membershipStatus(MembershipStatus.GENERAL)
                            .build()
                    );

            // 2. webtoon_payment 등록
            WebtoonPaymentEntity savedWebtoonPayment =
                    webtoonPaymentRepository.save(WebtoonPaymentEntity.builder()
                            .webtoonPaymentId(1L)
                            .memberId(member.getId())
                            .webtoonId(3L)
                            .webtoonDetailId(10L)
                            .cookiePaymentAmount(3L)
                            .build());
            Long requestId = member.getId();

            // when, then
            mockMvc.perform(get("/v1/webtoon-payments/" + requestId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusMessage").value("성공"))
                    .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                    .andExpect(jsonPath("$.data.webtoonNo").value(3L))
                    .andExpect(jsonPath("$.data.webtoonDetailNo").value(10L))
                    .andExpect(jsonPath("$.data.cookiePaymentAmount").value(3L));
        }

    }

}
