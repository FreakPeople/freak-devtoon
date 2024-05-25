package yjh.devtoon.promotion.domain.attribute;

import yjh.devtoon.webtoon.domain.WebtoonEntity;

public class Genre implements Attribute {

    private final String name;

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public boolean isApply(WebtoonEntity webtoon) {
        return webtoon.isGenre(name);
    }

}
