package yjh.devtoon.bad_words_warning_count.aplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.member.application.MemberService;
import yjh.devtoon.member.constant.ErrorMessage;
import yjh.devtoon.member.domain.MemberEntity;

@RequiredArgsConstructor
@Service
public class BadWordsWarningCountService {

    private final MemberService memberService;
    private final BadWordsWarningCountRepository badWordsWarningCountRepository;

    public BadWordsWarningCountEntity retrieve(final Long memberId) {
        MemberEntity member = memberService.retrieve(memberId);

        return badWordsWarningCountRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(memberId)));
    }

    @Transactional
    public BadWordsWarningCountEntity increase(final Long memberId) {
        BadWordsWarningCountEntity badWordsWarningCount = retrieve(memberId);

        badWordsWarningCount.increase();

        return badWordsWarningCountRepository.save(badWordsWarningCount);
    }

    public BadWordsWarningCountEntity retrieveMyInfo(final String memberEmail) {
        MemberEntity member = memberService.retrieveMyInfo(memberEmail);

        return badWordsWarningCountRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(member.getId())));
    }
}
