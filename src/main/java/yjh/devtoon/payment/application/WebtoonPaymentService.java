package yjh.devtoon.payment.application;

import static java.lang.Math.max;

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
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;
import yjh.devtoon.payment.dto.request.WebtoonPaymentCreateRequest;
import yjh.devtoon.payment.infrastructure.WebtoonPaymentRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.infrastructure.PromotionAttributeRepository;
import yjh.devtoon.promotion.infrastructure.PromotionRepository;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebtoonPaymentService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonViewerRepository webtoonViewerRepository;
    private final CookiePolicyRepository cookiePolicyRepository;
    private final CookieWalletService cookieWalletService;
    private final CookieWalletRepository cookieWalletRepository;
    private final WebtoonPaymentRepository webtoonPaymentRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionAttributeRepository promotionAttributeRepository;
    private final WebtoonService webtoonService;

    /**
     * 웹툰 미리보기 결제
     * : 웹툰 미리보기는 쿠키로 결제한다.
     */
    @Transactional
    public void register(final WebtoonPaymentCreateRequest request) {
        // 1. webtoonViewerId 조회
        Long webtoonViewerId = getWebtoonViewerIdOrThrow(request.getWebtoonViewerId());

        // 2. webtoonId 조회
        Long webtoonId = getWebtoonIdOrThrow(request.getWebtoonId());
        WebtoonEntity webtoon = webtoonService.retrieve(webtoonId);

        // 3. 웹툰 미리보기 1편당 차감 쿠키 개수 정책 조회
        Integer activeCookieQuantityPerEpisode =
                cookiePolicyRepository.findActiveCookieQuantityPerEpisode();

        // 4. 현재 적용 가능한 프로모션 중
        LocalDateTime now = LocalDateTime.now();
        List<PromotionEntity> activePromotions = promotionRepository.findActivePromotions(now);

        // 4-1. COOKIE_QUANTITY_DISCOUNT에 해당하는 프로모션 조회
        List<PromotionEntity> cookieQuantityDiscountActivePromotion = activePromotions.stream()
                .filter(PromotionEntity::isCookieQuantityDiscountApplicable)
                .toList();

        List<PromotionEntity> isApplicable = new ArrayList<>();
        for (PromotionEntity promotionEntity : cookieQuantityDiscountActivePromotion) {
            List<PromotionAttributeEntity> promotionAttributeEntities =
                    promotionAttributeRepository.findByPromotionEntityId(promotionEntity.getId());

            boolean isApplyPromotionAttributes = promotionAttributeEntities.stream()
                    .anyMatch(a -> a.isCookieQuantyDiscountApply(webtoon));
            if (isApplyPromotionAttributes) {
                isApplicable.add(promotionEntity);
            }
        }

        // 5. 웹툰 구매시 쿠키 할인 개수
        Integer discountQuantity = isApplicable.stream()
                .map(PromotionEntity::getDiscountQuantity)
                .reduce(0, Integer::sum);

        // 6. 필요한 최종 쿠키 개수 : 정책 - 5번
        int totalCookieQuantityPerEpisode = max(0,
                activeCookieQuantityPerEpisode - discountQuantity);

        // 7. cookieWallet 결제한 만큼 감소 후 DB 저장
        CookieWalletEntity cookieWallet = cookieWalletService.retrieve(webtoonViewerId);
        cookieWallet.decrease(totalCookieQuantityPerEpisode);
        cookieWalletRepository.save(cookieWallet);

        // 8. webtoonPaymentEntity 생성 후 DB 저장
        WebtoonPaymentEntity webtoonPayment = WebtoonPaymentEntity.create(
                webtoonViewerId,
                webtoonId,
                request.getWebtoonDetailId(),
                Long.valueOf(totalCookieQuantityPerEpisode)
        );
        webtoonPaymentRepository.save(webtoonPayment);

    }

    private Long getWebtoonViewerIdOrThrow(final Long webtoonViewerId) {
        return webtoonViewerRepository.findById(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.WEBTOON_VIEWER,
                                webtoonViewerId))
                ).getId();
    }

    private Long getWebtoonIdOrThrow(final Long webtoonId) {
        return webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.WEBTOON, webtoonId))
                ).getId();
    }

    /**
     * $
     * 특정 회원 웹툰 결제 내역 단건 조회
     */
    public WebtoonPaymentEntity retrieve(final Long webtoonViewerId) {
        return webtoonPaymentRepository.findByWebtoonViewerId(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getResourceNotFound(ResourceType.WEBTOON_PAYMENT,
                                webtoonViewerId)));
    }

}
