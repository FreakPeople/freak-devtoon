package yjh.devtoon.webtoon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 [Webtoon]")
class WebtoonEntityTest {

    @DisplayName("[create() 테스트] : 웬툰 엔티티 생성 테스트")
    @Test
    void createWebtoon_successfully() {
        assertThatCode(() -> WebtoonEntity.create("title", "writer_name", Genre.HORROR))
                .doesNotThrowAnyException();
    }

    @DisplayName("[create() 테스트] : 웬툰 엔티티 생성 필드 테스트")
    @Test
    void createWebtoon_successfully_and_validateField() {
        // when
        WebtoonEntity webtoonEntity = WebtoonEntity.create("title", "writer_name", Genre.HORROR);

        // then
        assertAll(
                () -> assertThat(webtoonEntity.getTitle()).isEqualTo("title"),
                () -> assertThat(webtoonEntity.getWriterName()).isEqualTo("writer_name"),
                () -> assertThat(webtoonEntity.getWriterName()).isNotEqualTo("failure")
        );
    }

}