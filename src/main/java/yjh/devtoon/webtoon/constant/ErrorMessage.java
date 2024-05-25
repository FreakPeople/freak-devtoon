package yjh.devtoon.webtoon.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorMessage {

    private static final String ID_NOT_FOUND = "id : '%d' 를 찾을 수 없습니다.";
    private static final String TITLE_CONFLICT = "title : '%s' 가 존재합니다.";
    private static final String GENRE_NOT_FOUND = "genre : '%s'를 찾을 수 없습니다.";

    public static String getWebtoonNotFound(final Long id) {
        return String.format(ID_NOT_FOUND, id);
    }

    public static String getTitleConflict(final String title) {
        return String.format(TITLE_CONFLICT, title);
    }

    public static String getGenreNotFound(final String genre) {
        return String.format(GENRE_NOT_FOUND, genre);
    }

}
