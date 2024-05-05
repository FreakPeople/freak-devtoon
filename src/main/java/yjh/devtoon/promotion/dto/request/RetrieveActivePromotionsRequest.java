package yjh.devtoon.promotion.dto.request;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class RetrieveActivePromotionsRequest {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public RetrieveActivePromotionsRequest(
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
