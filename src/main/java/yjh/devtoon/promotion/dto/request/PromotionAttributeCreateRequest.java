package yjh.devtoon.promotion.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PromotionAttributeCreateRequest {

    @NotBlank(message = "attribute_name을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String attributeName;

    @NotBlank(message = "attribute_value을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String attributeValue;

    public PromotionAttributeCreateRequest(
            final String attributeName,
            final String attributeValue
    ) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
