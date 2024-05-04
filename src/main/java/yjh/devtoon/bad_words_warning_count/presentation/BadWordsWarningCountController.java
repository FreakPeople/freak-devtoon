package yjh.devtoon.bad_words_warning_count.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.bad_words_warning_count.aplication.BadWordsWarningCountService;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.dto.response.BadWordsWarningCountResponse;
import yjh.devtoon.common.response.Response;

@RequestMapping("/v1/bad-words-warning-count")
@RequiredArgsConstructor
@RestController
public class BadWordsWarningCountController {

    private final BadWordsWarningCountService badWordsWarningCountService;

    /**
     * 비속어 카운트 조회
     */
    @GetMapping
    public ResponseEntity<Response> retrieve(
            @RequestParam("webtoonViewerNo") final Long id
    ) {
        BadWordsWarningCountEntity badWordsWarningCount = badWordsWarningCountService.retrieve(id);
        BadWordsWarningCountResponse response = BadWordsWarningCountResponse.from(badWordsWarningCount);
        return ResponseEntity.ok(Response.success(response));
    }

}
