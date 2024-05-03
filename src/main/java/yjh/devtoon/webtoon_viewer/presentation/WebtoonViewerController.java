package yjh.devtoon.webtoon_viewer.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.Response;
import yjh.devtoon.webtoon_viewer.application.WebtoonViewerService;
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
    public ResponseEntity<Response> register(
            @RequestBody @Valid final WebtoonViewerRegisterRequest request
    ) {
        webtoonViewerService.register(request);
        return ResponseEntity.ok(Response.success(null));
    }

    /**
     * 웹툰 구독자 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response> retrieve(
            @PathVariable final Long id
    ) {
        WebtoonViewerResponse webtoonViewerResponse = webtoonViewerService.retrieve(id);
        return ResponseEntity.ok(Response.success(webtoonViewerResponse));
    }

}
