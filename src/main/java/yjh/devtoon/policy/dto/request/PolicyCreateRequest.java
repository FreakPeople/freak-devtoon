package yjh.devtoon.policy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Map;

@AllArgsConstructor
@Getter
public class PolicyCreateRequest {

    private final String type; // 정책 유형 (예: "cookie", "bad_words")
    private final Map<String, Object> details; // 정책 세부 사항

}
