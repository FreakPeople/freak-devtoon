package yjh.devtoon.webtoon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiReponse;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import yjh.devtoon.webtoon.dto.response.WebtoonResponse;

@RequestMapping("/v1/webtoons")
@RequiredArgsConstructor
@RestController
public class WebtoonController {

    private final WebtoonService webtoonService;

    /**
     * 웹툰 등록
     */
    @PostMapping
    public ResponseEntity<ApiReponse> registerWebtoon(
            @RequestBody @Valid final WebtoonCreateRequest request
    ) {
        webtoonService.createWebtoon(request);
        return ResponseEntity.ok(ApiReponse.success(null));
    }

    /**
     * 웹툰 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiReponse> retrieve(
        @PathVariable final Long id
    ) {
        WebtoonEntity webtoon = webtoonService.retrieve(id);
        WebtoonResponse response = WebtoonResponse.from(webtoon);
        return ResponseEntity.ok(ApiReponse.success(response));
    }

}
