package yjh.devtoon.bad_words_warning_count.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;

@RequiredArgsConstructor
@Getter
public class BadWordsWarningCountResponse {

    private final Long memberId;
    private final int count;

    public static BadWordsWarningCountResponse from(final BadWordsWarningCountEntity badWordsWarningCountEntity) {
        return new BadWordsWarningCountResponse(
                badWordsWarningCountEntity.getMemberId(),
                badWordsWarningCountEntity.getCount()
        );
    }

}
