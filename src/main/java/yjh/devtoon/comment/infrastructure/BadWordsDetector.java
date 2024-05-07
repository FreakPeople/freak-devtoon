package yjh.devtoon.comment.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * case1. 외부 api 호출로 3초이상 오래 소요 되는 작업.
 * case2. 내부에서 직접 알고리즘을 사용하여 무겁고 오래걸리는 작업.
 * case3. 만약 해당 로직에서 장애가 발생하면?
 */
@Slf4j
@Component
public class BadWordsDetector {

    public int detectUsingExternalApi(String content) {

        // 비속어 검출 : 시간이 오래 소요 되는 작업
        try {
            Thread.sleep(3000);
        }catch (Exception e) {
            log.info(e.getMessage());
        }

        return 0;
    }

}