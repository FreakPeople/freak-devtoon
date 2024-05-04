package yjh.devtoon.promotion.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.Response;
import yjh.devtoon.promotion.application.PromotionService;
import yjh.devtoon.promotion.dto.request.PromotionCreateRequest;
import yjh.devtoon.promotion.dto.response.PromotionSoftDeleteResponse;

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

}
