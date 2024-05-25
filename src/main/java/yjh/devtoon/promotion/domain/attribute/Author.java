package yjh.devtoon.promotion.domain.attribute;

import yjh.devtoon.webtoon.domain.WebtoonEntity;

public class Author implements Attribute {

    private final String name;

    public Author(String name) {
        this.name = name;
    }

    @Override
    public boolean isApply(WebtoonEntity webtoon) {
        return webtoon.isWriter(name);
    }

}
