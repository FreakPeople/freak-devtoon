package yjh.devtoon.cookie_wallet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.cookie_wallet.domain.CookieWalletEntity;
import yjh.devtoon.cookie_wallet.dto.request.CookieRequest;
import yjh.devtoon.cookie_wallet.infrastructure.CookieWalletRepository;
import yjh.devtoon.member.application.MemberService;
import yjh.devtoon.member.constant.ErrorMessage;
import yjh.devtoon.member.domain.MemberEntity;

@RequiredArgsConstructor
@Service
public class CookieWalletService {

    private final MemberService memberService;
    private final CookieWalletRepository cookieWalletRepository;

    public CookieWalletEntity retrieve(final Long memberId) {
        MemberEntity member = memberService.retrieve(memberId);

        return cookieWalletRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(memberId)));
    }

    @Transactional
    public CookieWalletEntity increase(final Long memberId, final CookieRequest request) {
        MemberEntity member = memberService.retrieve(memberId);

        CookieWalletEntity cookieWallet = cookieWalletRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(memberId)));

        cookieWallet.increase(request.getQuantity());

        return cookieWalletRepository.save(cookieWallet);
    }

    @Transactional
    public CookieWalletEntity decrease(final Long memberId, final CookieRequest request) {
        MemberEntity member = memberService.retrieve(memberId);

        CookieWalletEntity cookieWallet = cookieWalletRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(memberId)));

        cookieWallet.decrease(request.getQuantity());

        return cookieWalletRepository.save(cookieWallet);
    }

    public CookieWalletEntity retrieveMyInfo(final String memberEmail) {
        MemberEntity member = memberService.retrieveMyInfo(memberEmail);

        return cookieWalletRepository.findById(member.getId())
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND, ErrorMessage.getMemberNotFound(member.getId())));
    }
}