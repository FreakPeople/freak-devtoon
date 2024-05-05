package yjh.devtoon.comment.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorMessage {

    private static final String ID_NOT_FOUND = "id : '%d' 를 찾을 수 없습니다.";

    public static String getCommentNotFound(final Long id) {
        return String.format(ID_NOT_FOUND, id);
    }

}