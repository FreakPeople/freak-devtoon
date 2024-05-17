package yjh.devtoon.promotion.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiReponse;
import yjh.devtoon.promotion.application.PromotionService;
import yjh.devtoon.promotion.domain.PromotionEntity;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;
import yjh.devtoon.promotion.dto.response.RetrieveActivePromotionsResponse;

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
    public ResponseEntity<ApiReponse> register(
            @RequestBody @Valid final PromotionCreateRequest request
    ) {
        promotionService.register(request);
        return ResponseEntity.ok(ApiReponse.success(null));
    }

    /**
     * 프로모션 삭제
     * : 삭제 시간을 통해 로직상에서 삭제 처리를 구분합니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiReponse> delete(
            @PathVariable final Long id
    ) {
        PromotionEntity promotionEntity = promotionService.delete(id);
        PromotionSoftDeleteResponse response = PromotionSoftDeleteResponse.from(promotionEntity);
        return ResponseEntity.ok(ApiReponse.success(response));
    }

    /**
     * 현재 활성화된 프로모션 전체 조회
     * : 현재 활성화된 프로모션이 없는 경우 빈 페이지를 반환합니다.
     */
    @GetMapping("/now")
    public ResponseEntity<ApiReponse<Page<RetrieveActivePromotionsResponse>>> retrieveActivePromotions(
            Pageable pageable
    ) {
        Page<RetrieveActivePromotionsResponse> activePromotions =
                promotionService.retrieveActivePromotions(pageable);
        return ResponseEntity.ok(ApiReponse.success(activePromotions));
    }

}
