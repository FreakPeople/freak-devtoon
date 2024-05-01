package yjh.devtoon.webtoon.dto.request;

import lombok.Getter;

@Getter
public class WebtoonCreateRequest {
    private final String title;
    private final String writerName;

    public WebtoonCreateRequest(
            final String title,
            final String writerName
    ) {
        this.title = title;
        this.writerName = writerName;
    }

}
