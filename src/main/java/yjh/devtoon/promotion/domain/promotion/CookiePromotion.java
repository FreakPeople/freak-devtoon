package yjh.devtoon.promotion.domain.promotion;

import yjh.devtoon.promotion.domain.attribute.Attribute;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import java.util.List;

public class CookiePromotion implements Promotion {

    private Integer discountQuantity;
    private List<Attribute> attributes; //

    public CookiePromotion(
            Integer discountQuantity,
            List<Attribute> attributes
    ) {
        this.discountQuantity = discountQuantity;
        this.attributes = attributes;
    }

    /**
     * 할인 수량을 반환하는 메서드
     * : 모든 속성이 충족되어야만 쿠키 개수가 할인됩니다.
     */
    @Override
    public int calculateDiscount(WebtoonEntity webtoon) {
        boolean isPossible = attributes.stream()
                .allMatch(a -> a.isApply(webtoon));
        if (isPossible) {
            return discountQuantity;
        }
        return 0;
    }

}
