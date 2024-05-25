package yjh.devtoon.webtoon.domain;

import lombok.Getter;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon.constant.ErrorMessage;
import java.util.Arrays;

@Getter
public enum Genre {
    ACTION("action"),
    COMEDY("comedy"),
    ROMANCE("romance"),
    HORROR("horror"),
    THRILLER("thriller"),
    CRIME("crime");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static Genre create(final String genre) {
        return Arrays.stream(Genre.values())
                .filter(g -> g.getName().equals(genre))
                .findFirst()
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getGenreNotFound(genre)));
    }

    public boolean isSame(final String attributeValue) {
        return name.equals(attributeValue);
    }
}
