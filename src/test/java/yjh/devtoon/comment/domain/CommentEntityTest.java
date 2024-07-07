package yjh.devtoon.comment.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 [Comment]")
class CommentEntityTest {

    @DisplayName("[create() 테스트] : 댓글 엔티티 생성 테스트")
    @Test
    void createComment_successfully() {
        assertThatCode(() -> CommentEntity.create(
                        1L,
                        1L,
                        "작성자 이름",
                        "작화가 너무 좋아요!"
                )
        ).doesNotThrowAnyException();
    }

}