package yjh.devtoon.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.cookie_wallet.application.CookieWalletService;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.payment.constant.ErrorMessage;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import yjh.devtoon.payment.dto.request.CookiePaymentCreateRequest;
import yjh.devtoon.payment.infrastructure.CookiePaymentRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CookiePaymentService {

    private final CookiePaymentRepository cookiePaymentRepository;
    private final WebtoonViewerRepository webtoonViewerRepository;
    private final CookiePolicyRepository cookiePolicyRepository;
    private final PromotionRepository promotionRepository;
    private final CookieWalletService cookieWalletService;
    private final CookieWalletRepository cookieWalletRepository;

    /**
     * 쿠키 결제
     * : 쿠키는 현금으로 결제한다.
     */
    @Transactional
    public void register(final CookiePaymentCreateRequest request) {

        // 1. webtoonViewerId가 DB에 존재하는지 확인
        Long webtoonViewerId = getWebtoonViewerIdOrThrow(request.getWebtoonViewerId());

        // 2. cookie policy에서 현재 cookie 가격 조회
        LocalDateTime now = LocalDateTime.now();
        BigDecimal activeCookiePrice = cookiePolicyRepository.activeCookiePrice(now);
        log.info("현재 적용되는 쿠키 가격은 {}", activeCookiePrice);

        // 3. 현재 활성 프로모션 조회
        List<PromotionEntity> activePromotions = promotionRepository.activePromotions(now);
        log.info("현재 적용되는 프로모션 리스트 {}", activePromotions);


        // 4. 쿠키 결제 가격 계산 - 요청한 쿠키 개수 * 현재 쿠키 가격 - 프로모션 할인율(회원등급,각종 프로모션 적용)
        Integer totalDiscountRate = calculateTotalDiscountRate(activePromotions);
        Integer quantity = request.getQuantity();
        BigDecimal totalPrice = activeCookiePrice.multiply(new BigDecimal(quantity));
        Integer totalDiscountRatePercentage = 1 - totalDiscountRate;
        BigDecimal discountedTotalPrice = totalPrice.multiply(new BigDecimal(totalDiscountRatePercentage));

        // 5. cookiePaymentEntity 생성 후 DB저장
        CookiePaymentEntity cookiePayment = CookiePaymentEntity.create(
                webtoonViewerId,
                quantity,
                activeCookiePrice,
                totalDiscountRatePercentage
        );
        cookiePaymentRepository.save(cookiePayment);

        // 6. webtoonViewerId에 해당하는 cookie wallet 조회 후 quantity만큼 증가
        CookieWalletEntity cookieWallet = cookieWalletService.retrieve(webtoonViewerId);
        cookieWallet.increase(quantity);
        cookieWalletRepository.save(cookieWallet);

    }

    private Long getWebtoonViewerIdOrThrow(final Long webtoonViewerId) {
        return webtoonViewerRepository.findById(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("WebtoonViewerNo", webtoonViewerId))
                ).getId();
    }

    private Integer calculateTotalDiscountRate(List<PromotionEntity> promotions) {

        /**
         * TODO 현재 적용 가능한 프로모션 할인율 계산 로직 적용
         */
        // 임시로 각 프로모션에 5% 할인율 적용
        Integer totalDiscountRate = 0;
        for (PromotionEntity promotion : promotions) {
            totalDiscountRate += 5;
        }
        totalDiscountRate /= 100;
        System.out.println("할인율: " + totalDiscountRate);

        return totalDiscountRate;
    }

}
