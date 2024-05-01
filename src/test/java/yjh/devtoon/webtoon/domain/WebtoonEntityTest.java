package yjh.devtoon.webtoon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;

class WebtoonEntityTest {

    @DisplayName("[create() 테스트] : 웬툰 엔티티 생성 테스트")
    @Test
    void create() {
        // given
        WebtoonCreateRequest webtoonCreateRequest = new WebtoonCreateRequest("title", "writer_name");

        assertThatCode(() -> WebtoonEntity.create(webtoonCreateRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("[create() 테스트] : 웬툰 엔티티 생성 필드 테스트")
    @Test
    void create_field() {
        // given
        WebtoonCreateRequest webtoonCreateRequest = new WebtoonCreateRequest("title", "writer_name");

        // when
        WebtoonEntity webtoonEntity = WebtoonEntity.create(webtoonCreateRequest);

        // then
        assertAll(
                () -> assertThat(webtoonEntity.getTitle()).isEqualTo("title"),
                () -> assertThat(webtoonEntity.getWriterName()).isEqualTo("writer_name"),
                () -> assertThat(webtoonEntity.getWriterName()).isNotEqualTo("failure")
        );
    }
}