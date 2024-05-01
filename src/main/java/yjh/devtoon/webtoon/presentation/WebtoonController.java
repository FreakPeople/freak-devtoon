package yjh.devtoon.webtoon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;

@RequestMapping("/v1/webtoons")
@RequiredArgsConstructor
@RestController
public class WebtoonController {
    private final WebtoonService webtoonService;

    /**
     * 웹툰 등록
     */
    @PostMapping
    public ResponseEntity<Void> registerWebtoon(
            @RequestBody final WebtoonCreateRequest request
    ) {
        webtoonService.createWebtoon(request);
        return ResponseEntity.ok(null);
    }
}
