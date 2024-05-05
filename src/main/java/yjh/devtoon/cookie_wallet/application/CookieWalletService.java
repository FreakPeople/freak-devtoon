package yjh.devtoon.cookie_wallet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.dto.request.CookieRequest;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.webtoon_viewer.application.WebtoonViewerService;
import yjh.devtoon.webtoon_viewer.constant.ErrorMessage;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;

@RequiredArgsConstructor
@Service
public class CookieWalletService {

    private final WebtoonViewerService webtoonViewerService;
    private final CookieWalletRepository cookieWalletRepository;

    public CookieWalletEntity retrieve(final Long webtoonViewerId) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(webtoonViewerId);

        return cookieWalletRepository.findById(webtoonViewer.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(webtoonViewerId)));
    }

    @Transactional
    public CookieWalletEntity increase(final Long webtoonViewerId, final CookieRequest request) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(webtoonViewerId);

        CookieWalletEntity cookieWallet = cookieWalletRepository.findById(webtoonViewer.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(webtoonViewerId)));

        cookieWallet.increase(request.getQuantity());

        return cookieWalletRepository.save(cookieWallet);
    }

    @Transactional
    public CookieWalletEntity decrease(final Long webtoonViewerId, final CookieRequest request) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(webtoonViewerId);

        CookieWalletEntity cookieWallet = cookieWalletRepository.findById(webtoonViewer.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(webtoonViewerId)));

        cookieWallet.decrease(request.getQuantity());

        return cookieWalletRepository.save(cookieWallet);
    }

}