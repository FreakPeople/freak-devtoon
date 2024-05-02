package yjh.devtoon.promotion.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 단위 테스트 [PromotionAttribute]")
class PromotionAttributeEntityTest {

    @Test
    @DisplayName("[create() 테스트] : 프로모션 속성 엔티티 생성 테스트")
    public void createPromotionAttributeEntityTest() {
        // given
        PromotionEntity promotionEntity = new PromotionEntity();
        String attributeName = "target-month";
        String attributeValue = "JUL";


        // when
        PromotionAttributeEntity attribute = PromotionAttributeEntity.create(
                promotionEntity, attributeName, attributeValue
        );

        // then
        assertAll("attribute",
                () -> assertEquals(promotionEntity, attribute.getPromotionEntity()),
                () -> assertEquals(attributeName, attribute.getAttributeName()),
                () -> assertEquals(attributeValue, attribute.getAttributeValue()),
                () -> assertNull(attribute.getDeletedAt())
        );

    }

}