package yjh.devtoon.webtoon_viewer.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.webtoon_viewer.application.WebtoonViewerService;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import yjh.devtoon.webtoon_viewer.dto.request.MembershipStatusChangeRequest;
import yjh.devtoon.webtoon_viewer.dto.request.WebtoonViewerRegisterRequest;
import yjh.devtoon.webtoon_viewer.dto.response.WebtoonViewerResponse;

@RequestMapping("/v1/webtoon-viewers")
@RequiredArgsConstructor
@RestController
public class WebtoonViewerController {

    private final WebtoonViewerService webtoonViewerService;

    /**
     * 웹툰 구독자 회원 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid final WebtoonViewerRegisterRequest request
    ) {
        webtoonViewerService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 웹툰 구독자 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieve(
            @PathVariable final Long id
    ) {
        WebtoonViewerEntity webtoonViewer = webtoonViewerService.retrieve(id);
        WebtoonViewerResponse response = WebtoonViewerResponse.from(webtoonViewer);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 웹툰 구독자 회원 등급 변경
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> changeMembershipStatus(
            @PathVariable final Long id,
            @RequestBody @Valid final MembershipStatusChangeRequest request
    ) {
        webtoonViewerService.changeMembershipStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
