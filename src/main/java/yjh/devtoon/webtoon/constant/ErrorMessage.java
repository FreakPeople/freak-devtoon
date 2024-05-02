package yjh.devtoon.webtoon.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorMessage {

    public static final String ID_NOT_FOUND = "id : '%d' 를 찾을 수 없습니다.";
    public static final String TITLE_CONFLICT = "title : '%s' 가 존재합니다.";

    public static String ID_NOT_FOUND(final Long id) {
        return String.format(ID_NOT_FOUND, id);
    }

    public static String TITLE_CONFLICT(final String title) {
        return String.format(TITLE_CONFLICT, title);
    }

}
