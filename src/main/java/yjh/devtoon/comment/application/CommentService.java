package yjh.devtoon.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.aplication.BadWordsWarningCountService;
import yjh.devtoon.comment.constant.ErrorMessage;
import yjh.devtoon.comment.domain.CommentEntity;
import yjh.devtoon.comment.dto.request.CommentCreateRequest;
import yjh.devtoon.comment.infrastructure.BadWordsDetector;
import yjh.devtoon.comment.infrastructure.CommentRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final WebtoonService webtoonService;
    private final BadWordsWarningCountService badWordsWarningCountService;
    private final CommentRepository commentRepository;
    private final BadWordsDetector badWordsDetector;

    public CommentEntity retrieve(final Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getCommentNotFound(id)));
    }

    @Transactional
    public void create(final CommentCreateRequest request) {
        /**
         * 3초 이상의 오래 걸리는 작업.
         */
        validateBadWords(request);

        WebtoonEntity webtoon = webtoonService.retrieve(request.getWebtoonId());
        CommentEntity comment = CommentEntity.create(
                webtoon.getId(),
                request.getDetailId(),
                request.getWebtoonViewerId(),
                request.getContent()
        );
        commentRepository.save(comment);
    }

    private void validateBadWords(final CommentCreateRequest request) {
        int badWordsCount = badWordsDetector.detectUsingExternalApi(request.getContent());
        if (badWordsCount > 0) {
            badWordsWarningCountService.increase(request.getWebtoonViewerId());
        }
    }

}