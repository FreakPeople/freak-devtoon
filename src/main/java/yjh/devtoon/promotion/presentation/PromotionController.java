package yjh.devtoon.promotion.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.promotion.application.PromotionService;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;
import yjh.devtoon.promotion.dto.response.RetrieveActivePromotionAttributesResponse;
import yjh.devtoon.promotion.dto.response.RetrieveActivePromotionsResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
@RestController
public class PromotionController {

    private final PromotionService promotionService;

    /**
     * 프로모션 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid final PromotionCreateRequest request
    ) {
        promotionService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 프로모션 삭제
     * : 삭제 시간을 통해 로직상에서 삭제 처리를 구분합니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable final Long id
    ) {
        PromotionEntity promotionEntity = promotionService.delete(id);
        PromotionSoftDeleteResponse response = PromotionSoftDeleteResponse.from(promotionEntity);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 현재 적용 가능한 모든 프로모션 조회
     * : 프로모션만 조회합니다. 현재 적용 가능한 모든 프로모션이 없는 경우 빈 리스트를 반환합니다.
     */
    @GetMapping("/now")
    public ResponseEntity<ApiResponse<List<RetrieveActivePromotionsResponse>>> retrieveActivePromotions() {
        List<PromotionEntity> activePromotions = promotionService.retrieveActivePromotions();
        List<RetrieveActivePromotionsResponse> response = activePromotions.stream()
                .map(RetrieveActivePromotionsResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 현재 적용 가능한 프로모션에 포함된 모든 프로모션 속성 조회
     * : 현재 적용 가능한 프로모션이 없는 경우 빈 리스트를 반환합니다.
     */
    @GetMapping("/now/{id}")
    public ResponseEntity<ApiResponse<List<RetrieveActivePromotionAttributesResponse>>> retrieveActivePromotionAttributes(
            @PathVariable final Long id
    ) {
        List<PromotionAttributeEntity> activePromotionAttributes =
                promotionService.retrieveActivePromotionAttributes(id);
        List<RetrieveActivePromotionAttributesResponse> response =
                activePromotionAttributes.stream()
                        .map(RetrieveActivePromotionAttributesResponse::from)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
