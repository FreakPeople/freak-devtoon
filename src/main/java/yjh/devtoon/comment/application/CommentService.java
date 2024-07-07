package yjh.devtoon.comment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.auth.util.SecurityUtil;
import yjh.devtoon.comment.constant.ErrorMessage;
import yjh.devtoon.comment.domain.CommentEntity;
import yjh.devtoon.comment.dto.request.CommentCreateRequest;
import yjh.devtoon.comment.infrastructure.BadWordsDetector;
import yjh.devtoon.comment.infrastructure.CommentRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.member.application.MemberService;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final WebtoonService webtoonService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final BadWordsDetector badWordsDetector;

    public CommentEntity retrieve(final Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getCommentNotFound(id)));
    }

    @Transactional
    public void create(final CommentCreateRequest request) {
        String memberEmail = SecurityUtil.getCurrentUsername()
                .orElseThrow(() -> new IllegalArgumentException("[Error] not found authentication username"));

        MemberEntity writer = memberService.retrieveMyInfo(memberEmail);

        /**
         * @Async를 활용한 비동기 동작
         */
        badWordsDetector.validateBadWords(request, writer.getId());

        WebtoonEntity webtoon = webtoonService.retrieve(request.getWebtoonId());

        CommentEntity comment = CommentEntity.create(
                webtoon.getId(),
                writer.getId(),
                request.getContent()
        );
        commentRepository.save(comment);
    }

    public Page<CommentEntity> retrieveAll(
            final Long webtoonId,
            final Pageable pageable
    ) {
        return commentRepository.findByWebtoonId(webtoonId, pageable);
    }
}