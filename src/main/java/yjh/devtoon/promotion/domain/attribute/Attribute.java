package yjh.devtoon.promotion.domain.attribute;

import yjh.devtoon.webtoon.domain.WebtoonEntity;

public interface Attribute {
    boolean isApply(WebtoonEntity webtoon);
}
