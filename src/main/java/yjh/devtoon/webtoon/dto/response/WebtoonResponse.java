package yjh.devtoon.webtoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WebtoonResponse {

    private Long webtoonId;
    private String title;
    private String writerName;
    private String imageUrl;
    private String genre;
    private LocalDateTime createdAt;

    public static WebtoonResponse from(final WebtoonEntity webtoonEntity) {
        WebtoonResponse webtoonResponse = new WebtoonResponse();
        webtoonResponse.webtoonId = webtoonEntity.getId();
        webtoonResponse.title = webtoonEntity.getTitle();
        webtoonResponse.writerName = webtoonEntity.getWriterName();
        webtoonResponse.imageUrl = webtoonEntity.getImageUrl();
        webtoonResponse.genre = webtoonEntity.getGenre().getName();
        webtoonResponse.createdAt = webtoonEntity.getCreatedAt();
        return webtoonResponse;
    }

}
