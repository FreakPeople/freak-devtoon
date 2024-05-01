package yjh.devtoon.webtoon.domain;

import lombok.Getter;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;

@Getter
public class WebtoonEntity {
    private String title;
    private String writerName;

    public WebtoonEntity(final String title, final String writerName) {
        this.title = title;
        this.writerName = writerName;
    }

    public static WebtoonEntity create(final WebtoonCreateRequest webtoonCreateRequest) {
        return new WebtoonEntity(
                webtoonCreateRequest.getTitle(),
                webtoonCreateRequest.getWriterName()
        );
    }

}
