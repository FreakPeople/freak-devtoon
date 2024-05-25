package yjh.devtoon.promotion.domain.promotion;

import yjh.devtoon.webtoon.domain.WebtoonEntity;

public interface Promotion {
    int calculateDiscount(WebtoonEntity webtoon);
}
