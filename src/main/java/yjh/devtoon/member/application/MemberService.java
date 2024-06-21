package yjh.devtoon.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;
import yjh.devtoon.bad_words_warning_count.infrastructure.BadWordsWarningCountRepository;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.member.constant.ErrorMessage;
import yjh.devtoon.member.domain.Authority;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.Role;
import yjh.devtoon.member.dto.request.MemberRegisterRequest;
import yjh.devtoon.member.dto.request.MembershipStatusChangeRequest;
import yjh.devtoon.member.infrastructure.MemberRepository;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BadWordsWarningCountRepository badWordsWarningCountRepository;
    private final CookieWalletRepository cookieWalletRepository;

    @Transactional
    public void register(final MemberRegisterRequest request) {
        validateEmailDuplicated(request.getEmail());

        MemberEntity member = MemberEntity.create(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                MembershipStatus.GENERAL,
                Set.of(new Authority(Role.MEMBER))
        );
        MemberEntity savedMember = memberRepository.save(member);

        BadWordsWarningCountEntity badWordsWarningCount = BadWordsWarningCountEntity.create(savedMember);
        badWordsWarningCountRepository.save((badWordsWarningCount));

        CookieWalletEntity cookieWallet = CookieWalletEntity.create(savedMember);
        cookieWalletRepository.save(cookieWallet);
    }

    private void validateEmailDuplicated(final String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new DevtoonException(ErrorCode.CONFLICT, ErrorMessage.getEmailConflict(email));
                });
    }

    public MemberEntity retrieve(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(id)));
    }

    @Transactional
    public void changeMembershipStatus(final Long id, final MembershipStatusChangeRequest request) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(id)));

        String targetStatus = request.getMembershipStatus();
        member.change(MembershipStatus.create(targetStatus));
    }

}
