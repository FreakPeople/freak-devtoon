package yjh.devtoon.viewer.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.viewer.constant.ErrorMessage;
import yjh.devtoon.viewer.domain.MembershipStatus;
import yjh.devtoon.viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.viewer.dto.request.WebtoonViewerRegisterRequest;
import yjh.devtoon.viewer.infrastructure.WebtoonViewerRepository;

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
