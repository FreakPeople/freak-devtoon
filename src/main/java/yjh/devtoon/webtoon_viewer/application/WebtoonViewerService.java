package yjh.devtoon.webtoon_viewer.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon_viewer.constant.ErrorMessage;
import yjh.devtoon.webtoon_viewer.domain.MembershipStatus;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.dto.request.WebtoonViewerRegisterRequest;
import yjh.devtoon.webtoon_viewer.infrastructure.WebtoonViewerRepository;

@RequiredArgsConstructor
@Service
public class WebtoonViewerService {

    private final WebtoonViewerRepository webtoonViewerRepository;

    public void register(final WebtoonViewerRegisterRequest request) {
        validateEmailDuplicated(request.getEmail());

        WebtoonViewerEntity webtoonViewer = WebtoonViewerEntity.create(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                MembershipStatus.GENERAL
        );

        webtoonViewerRepository.save(webtoonViewer);
    }

    private void validateEmailDuplicated(final String email) {
        webtoonViewerRepository.findByEmail(email)
                .ifPresent(webtoonViewer -> {
                    throw new DevtoonException(ErrorCode.CONFLICT, ErrorMessage.EMAIL_CONFLICT(email));
                });
    }
}
