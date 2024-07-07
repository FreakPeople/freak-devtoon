package yjh.devtoon.comment.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import yjh.devtoon.bad_words_warning_count.aplication.BadWordsWarningCountService;
import yjh.devtoon.comment.dto.request.CommentCreateRequest;

/**
 * case1. 외부 api 호출로 3초이상 오래 소요 되는 작업.
 * case2. 내부에서 직접 알고리즘을 사용하여 무겁고 오래걸리는 작업.
 * case3. 만약 해당 로직에서 장애가 발생하면?
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BadWordsDetector {

    private final BadWordsWarningCountService badWordsWarningCountService;

    @Async("EventThreadPool")
    public void validateBadWords(
            final CommentCreateRequest request,
            final Long writerId
    ) {
        int badWordsCount = detectUsingExternalApi(request.getContent());

        if (badWordsCount > 0) {
            badWordsWarningCountService.increase(writerId);
        }
    }

    private int detectUsingExternalApi(String content) {
        // 비속어 검출 : 시간이 오래 소요 되는 작업
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return 0;
    }

}