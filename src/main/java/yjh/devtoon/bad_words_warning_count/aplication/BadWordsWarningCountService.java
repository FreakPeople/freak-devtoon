package yjh.devtoon.bad_words_warning_count.aplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon_viewer.application.WebtoonViewerService;
import yjh.devtoon.webtoon_viewer.constant.ErrorMessage;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;

@RequiredArgsConstructor
@Service
public class BadWordsWarningCountService {

    private final WebtoonViewerService webtoonViewerService;
    private final BadWordsWarningCountRepository badWordsWarningCountRepository;

    public BadWordsWarningCountEntity retrieve(final Long webtoonViewerId) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(webtoonViewerId);

        return badWordsWarningCountRepository.findById(webtoonViewer.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getWebtoonViewerNotFound(webtoonViewerId)));
    }

    @Transactional
    public BadWordsWarningCountEntity increase(final Long webtoonViewerId) {
        BadWordsWarningCountEntity badWordsWarningCount = retrieve(webtoonViewerId);

        badWordsWarningCount.increase();

        return badWordsWarningCountRepository.save(badWordsWarningCount);
    }

}
