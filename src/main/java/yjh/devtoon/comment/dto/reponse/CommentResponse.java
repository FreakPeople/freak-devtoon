package yjh.devtoon.comment.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.comment.domain.CommentEntity;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {

    private Long webtoonNo;
    private Long webtoonDetailNo;
    private Long webtoonViewerNo;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(final CommentEntity comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.webtoonNo = comment.getWebtoonId();
        commentResponse.webtoonDetailNo = comment.getDetailId();
        commentResponse.webtoonViewerNo = comment.getWebtoonViewerId();
        commentResponse.content = comment.getContent();
        commentResponse.createAt = comment.getCreatedAt();
        commentResponse.updatedAt = comment.getUpdatedAt();
        return commentResponse;
    }

}