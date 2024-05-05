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
import yjh.devtoon.common.response.Response;
import yjh.devtoon.promotion.application.PromotionService;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.request.RetrieveActivePromotionsRequest;
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
    public ResponseEntity<Response> register(
            @RequestBody @Valid final PromotionCreateRequest request
    ) {
        promotionService.register(request);
        return ResponseEntity.ok(Response.success(null));
    }

    /**
     * 프로모션 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> softDelete(
            @PathVariable final Long id
    ) {
        PromotionSoftDeleteResponse response = promotionService.softDelete(id);
        return ResponseEntity.ok(Response.success(response));
    }

    /**
     * 현재 적용 가능한 프로모션 전체 조회
     */
    @GetMapping("/now")
    public ResponseEntity<Response<Page<RetrieveActivePromotionsResponse>>> retrieveActivePromotions(
            @RequestBody final RetrieveActivePromotionsRequest request,
            Pageable pageable
    ) {
        Page<RetrieveActivePromotionsResponse> activePromotions = promotionService.retrieveActivePromotions(request, pageable);
        if (activePromotions.hasContent()) {
            activePromotions.getContent().forEach(promotion -> log.info("프로모션 상세 조회: {}", promotion));
        } else {
            log.info("조회된 프로모션 없음.");
        }

        return ResponseEntity.ok(Response.success(activePromotions));
    }

}
