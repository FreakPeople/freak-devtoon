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
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;
import yjh.devtoon.payment.dto.request.WebtoonPaymentCreateRequest;
import yjh.devtoon.payment.infrastructure.WebtoonPaymentRepository;
import yjh.devtoon.policy.infrastructure.CookiePolicyRepository;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;

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

        // 3. 웹툰 미리보기 1편당 차감 쿠키 개수 정책 조회
        Integer activeCookieQuantityPerEpisode = cookiePolicyRepository.findActiveCookieQuantityPerEpisode();

        // 4. webtoonViewerId의 cookieWallet 조회 - cookieWallet 결제한 만큼 감소 후 DB 저장
        CookieWalletEntity cookieWallet = cookieWalletService.retrieve(webtoonViewerId);
        cookieWallet.decrease(activeCookieQuantityPerEpisode);
        cookieWalletRepository.save(cookieWallet);

        // 5. webtoonPaymentEntity 생성 후 DB 저장
        WebtoonPaymentEntity webtoonPayment = WebtoonPaymentEntity.create(
                webtoonViewerId,
                webtoonId,
                request.getWebtoonDetailId(),
                Long.valueOf(activeCookieQuantityPerEpisode)
        );
        webtoonPaymentRepository.save(webtoonPayment);

    }

    private Long getWebtoonViewerIdOrThrow(final Long webtoonViewerId) {
        return webtoonViewerRepository.findById(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("WebtoonViewerNo", webtoonViewerId))
                ).getId();
    }

    private Long getWebtoonIdOrThrow(final Long webtoonId) {
        return webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new DevtoonException(
                        ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("WebtoonNo", webtoonId))
                ).getId();
    }

    /**
     * 특정 회원 웹툰 결제 내역 단건 조회
     */
    public WebtoonPaymentEntity retrieve(final Long webtoonViewerId) {
        return webtoonPaymentRepository.findByWebtoonViewerId(webtoonViewerId)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getResourceNotFound("특정 회원 WebtoonPayment 내역", webtoonViewerId)));
    }

}
