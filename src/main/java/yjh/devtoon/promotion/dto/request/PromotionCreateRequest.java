package yjh.devtoon.promotion.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PromotionCreateRequest {

    @NotBlank(message = "description을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String description;

    @NotNull(message = "시작 날짜는 필수입니다.")
    @FutureOrPresent(message = "시작 날짜는 현재 시간이나 그 이후여야 합니다.")
    private final LocalDateTime startDate;

    @FutureOrPresent(message = "종료 날짜는 현재 시간이나 그 이후여야 합니다.")
    private final LocalDateTime endDate;

    @NotBlank(message = "attribute_name을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String attributeName;

    @NotBlank(message = "attribute_value을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private final String attributeValue;

    public PromotionCreateRequest(
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String attributeName,
            String attributeValue
    ) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

}
