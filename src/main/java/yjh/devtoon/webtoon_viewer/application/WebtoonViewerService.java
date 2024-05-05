package yjh.devtoon.webtoon_viewer.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.webtoon_viewer.constant.ErrorMessage;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.dto.request.MembershipStatusChangeRequest;
import yjh.devtoon.webtoon_viewer.dto.request.WebtoonViewerRegisterRequest;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;

@RequiredArgsConstructor
@Service
public class WebtoonViewerService {

    private final WebtoonViewerRepository webtoonViewerRepository;
    private final BadWordsWarningCountRepository badWordsWarningCountRepository;
    private final CookieWalletRepository cookieWalletRepository;

    @Transactional
    public void register(final WebtoonViewerRegisterRequest request) {
        validateEmailDuplicated(request.getEmail());

        WebtoonViewerEntity webtoonViewer = WebtoonViewerEntity.create(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                MembershipStatus.GENERAL
        );
        WebtoonViewerEntity savedWebtoonViewer = webtoonViewerRepository.save(webtoonViewer);

        BadWordsWarningCountEntity badWordsWarningCount = BadWordsWarningCountEntity.create(savedWebtoonViewer);
        badWordsWarningCountRepository.save((badWordsWarningCount));

        CookieWalletEntity cookieWallet = CookieWalletEntity.create(savedWebtoonViewer);
        cookieWalletRepository.save(cookieWallet);
    }

    private void validateEmailDuplicated(final String email) {
        webtoonViewerRepository.findByEmail(email)
                .ifPresent(webtoonViewer -> {
                    throw new DevtoonException(ErrorCode.CONFLICT, ErrorMessage.getEmailConflict(email));
                });
    }

    public WebtoonViewerEntity retrieve(final Long id) {
        return webtoonViewerRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(id)));
    }

    @Transactional
    public void changeMembershipStatus(final Long id, final MembershipStatusChangeRequest request) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(id)));

        String targetStatus = request.getMembershipStatus();
        webtoonViewer.change(MembershipStatus.create(targetStatus));
    }

}
