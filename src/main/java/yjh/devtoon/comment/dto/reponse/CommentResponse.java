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
    private Long writerNo;
    private String writerName;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(final CommentEntity comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.webtoonNo = comment.getWebtoonId();
        commentResponse.writerNo = comment.getWriterId();
        commentResponse.writerName = comment.getWriterName();
        commentResponse.content = comment.getContent();
        commentResponse.createAt = comment.getCreatedAt();
        commentResponse.updatedAt = comment.getUpdatedAt();
        return commentResponse;
    }

}