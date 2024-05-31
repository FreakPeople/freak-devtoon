package yjh.devtoon.payment.application;

import static org.apache.commons.lang3.compare.ComparableUtils.min;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.common.utils.ResourceType;
import yjh.devtoon.cookie_wallet.application.CookieWalletService;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.payment.constant.ErrorMessage;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import yjh.devtoon.payment.domain.Price;
import yjh.devtoon.payment.dto.request.CookiePaymentCreateRequest;
import yjh.devtoon.payment.infrastructure.CookiePaymentRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.promotion.application.PromotionService;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.webtoon_viewer.application.WebtoonViewerService;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CookiePaymentService {

    private final CookiePaymentRepository cookiePaymentRepository;
    private final WebtoonViewerRepository webtoonViewerRepository;
    private final CookiePolicyRepository cookiePolicyRepository;
    private final CookieWalletService cookieWalletService;
    private final CookieWalletRepository cookieWalletRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;
    private final WebtoonViewerService webtoonViewerService;
    private final PromotionService promotionService;

    /**
     * 쿠키 결제
     * : 쿠키는 현금으로 결제한다.
     */
    @Transactional
    public void register(final CookiePaymentCreateRequest request) {
        // 1. webtoonViewerId가 DB에 존재하는지 확인
        Long webtoonViewerId = getWebtoonViewerIdOrThrow(request.getWebtoonViewerId());
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(webtoonViewerId);

        // 2. cookie policy에서 현재 cookie 가격 조회
        LocalDateTime now = LocalDateTime.now();
        Price activeCookie = Price.of(cookiePolicyRepository.activeCookiePrice(now));

        // 3. 현재 활성 프로모션 조회 (캐시)
        List<PromotionEntity> activePromotions = promotionService.retrieveActivePromotions();

        // 4. 3번 중 DiscountType이 CASH_DISCOUNT 인것만 골라냄
        List<PromotionEntity> cashDiscountActivePromotion = activePromotions.stream()
                .filter(PromotionEntity::isCashDiscount)
                .toList();

        // 5-1. 4번 중 중복 할인 가능한 프로모션 중 적용 가능한 것을 골라냄
        List<PromotionEntity> duplicatables = cashDiscountActivePromotion.stream()
                .filter(PromotionEntity::getIsDiscountDuplicatable)
                .toList();
        List<PromotionEntity> isApplyDuplicatables = new ArrayList<>();
        for (PromotionEntity promotionEntity : duplicatables) {
            List<PromotionAttributeEntity> promotionAttributeEntities =
                    promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId());

            boolean isApplyPromotionAttributes = promotionAttributeEntities.stream()
                    .anyMatch(a -> a.isCashDiscountApply(webtoonViewer, request.getQuantity()));
            if (isApplyPromotionAttributes) {
                isApplyDuplicatables.add(promotionEntity);
            }
        }

        // 5-2. 4번 중 중복 할인 불가능한 프로모션 중 적용 가능한 것을 골라냄
        List<PromotionEntity> notDuplicatables = cashDiscountActivePromotion.stream()
                .filter(p -> p.isNotDiscountDuplicatable())
                .toList();

        List<PromotionEntity> isApplyNotDuplicatables = new ArrayList<>();

        for (PromotionEntity promotionEntity : notDuplicatables) {
            List<PromotionAttributeEntity> promotionAttributeEntities =
                    promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId());

            boolean isApplyPromotionAttributes = promotionAttributeEntities.stream()
                    .anyMatch(a -> a.isCashDiscountApply(webtoonViewer, request.getQuantity()));

            if (isApplyPromotionAttributes) {
                isApplyNotDuplicatables.add(promotionEntity);
            }
        }

        // 6. 할인율: 중복 할인 불가능한 프로모션의 경우 가장 할인율이 큰 것을 골라냄
        BigDecimal maxRate = isApplyNotDuplicatables.stream()
                .map(PromotionEntity::getDiscountRate)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        // 7. 할인율: 중복 할인 가능한 프로모션의 경우 할인율을 더함.
        BigDecimal sumRate = isApplyDuplicatables.stream()
                .map(PromotionEntity::getDiscountRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalRate = min(BigDecimal.valueOf(50), maxRate.add(sumRate));

        // 8. 쿠키 결제 가격 계산 - 요청한 쿠키 개수 * 현재 쿠키 가격 - 프로모션 할인율
        BigDecimal totalDiscountRate = finalRate.divide(BigDecimal.valueOf(100));
        Integer quantity = request.getQuantity();
        Price totalPrice = activeCookie.calculateTotalPrice(quantity);
        BigDecimal netPaymentMultiplier = BigDecimal.ONE.subtract(totalDiscountRate);
        Price discountedTotalPrice = totalPrice.multiply(Price.of(netPaymentMultiplier));

        // 9. cookiePaymentEntity 생성 후 DB저장
        CookiePaymentEntity cookiePayment = CookiePaymentEntity.create(
                webtoonViewerId,
                quantity,
                activeCookie,
                totalDiscountRate
        );
        cookiePaymentRepository.save(cookiePayment);

        // 10. webtoonViewerId에 해당하는 cookie wallet 조회 후 quantity만큼 증가
        CookieWalletEntity cookieWallet = cookieWalletService.retrieve(webtoonViewerId);
        cookieWallet.increase(quantity);
        cookieWalletRepository.save(cookieWallet);
    }

    private Long getWebtoonViewerIdOrThrow(final Long webtoonViewerId) {
        return webtoonViewerRepository.findById(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.WEBTOON_VIEWER,
                                webtoonViewerId))
                ).getId();
    }

    /**
     * 특정 회원 쿠키 결제 내역 조회
     */
    public CookiePaymentEntity retrieve(final Long webtoonViewerId) {
        return cookiePaymentRepository.findByWebtoonViewerId(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(
                                ResourceType.COOKIE_PAYMENT,
                                webtoonViewerId
                        )));
    }

}
