package yjh.devtoon.viewer.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.Response;
import yjh.devtoon.viewer.application.WebtoonViewerService;
import yjh.devtoon.viewer.dto.request.WebtoonViewerRegisterRequest;

@RequestMapping("/v1/viewers")
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

}
