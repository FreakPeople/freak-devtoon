package yjh.devtoon.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentCreateRequest {

    private final Long webtoonId;

    @NotBlank(message = "content를 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 1, max = 100)
    private final String content;

}