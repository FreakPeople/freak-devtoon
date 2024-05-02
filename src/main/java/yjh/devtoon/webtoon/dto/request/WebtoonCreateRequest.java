package yjh.devtoon.webtoon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WebtoonCreateRequest {
    @NotBlank(message = "title을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 1, max = 20)
    private final String title;

    @NotBlank(message = "writerName을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 1, max = 20)
    private final String writerName;

    public WebtoonCreateRequest(
            final String title,
            final String writerName
    ) {
        this.title = title;
        this.writerName = writerName;
    }

}
