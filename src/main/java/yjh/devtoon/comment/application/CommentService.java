package yjh.devtoon.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.comment.constant.ErrorMessage;
import yjh.devtoon.comment.domain.CommentEntity;
import yjh.devtoon.comment.dto.request.CommentCreateRequest;
import yjh.devtoon.comment.infrastructure.CommentRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebtoonService webtoonService;

    public CommentEntity retrieve(final Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getCommentNotFound(id)));
    }

    @Transactional
    public void create(final CommentCreateRequest request) {

        WebtoonEntity webtoon = webtoonService.retrieve(request.getWebtoonId());

        CommentEntity comment = CommentEntity.create(
                webtoon.getId(),
                request.getDetailId(),
                request.getWebtoonViewerId(),
                request.getContent()
        );

        commentRepository.save(comment);
    }

}