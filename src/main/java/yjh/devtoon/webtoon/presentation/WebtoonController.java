package yjh.devtoon.webtoon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.webtoon.application.WebtoonService;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import yjh.devtoon.webtoon.dto.response.WebtoonResponse;
import java.nio.file.Files;

@RequestMapping("/v1/webtoons")
@RequiredArgsConstructor
@RestController
public class WebtoonController {

    private final WebtoonService webtoonService;

    /**
     * 웹툰 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> registerWebtoon(
            @RequestPart("request") @Valid final WebtoonCreateRequest request,
            @RequestPart("image") final MultipartFile multipartFile
    ) {
        webtoonService.createWebtoon(request, multipartFile);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 웹툰 이미지 조회
     */
    @GetMapping("/{id}/images/{fileName}")
    public ResponseEntity<Resource> retrieveImage(
            @PathVariable final Long id,
            @PathVariable final String fileName
    ) {
        Resource resource =  webtoonService.retrieveImage(id, fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            throw new IllegalArgumentException("[Error] Image reference failed");
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    /**
     * 웹툰 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieve(
            @PathVariable final Long id
    ) {
        WebtoonEntity webtoon = webtoonService.retrieve(id);
        WebtoonResponse response = WebtoonResponse.from(webtoon);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 웹툰 전체 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAll(Pageable pageable) {
        Page<WebtoonEntity> webtoons = webtoonService.retrieveAll(pageable);
        Page<WebtoonResponse> webtoonsResponse = webtoons.map(WebtoonResponse::from);
        return ResponseEntity.ok(ApiResponse.success(webtoonsResponse));
    }

}
