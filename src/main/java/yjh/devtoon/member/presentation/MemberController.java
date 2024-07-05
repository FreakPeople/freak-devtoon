package yjh.devtoon.member.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.member.application.MemberService;
import yjh.devtoon.member.domain.MemberEntity;
import yjh.devtoon.member.dto.request.MemberRegisterRequest;
import yjh.devtoon.member.dto.request.MembershipStatusChangeRequest;
import yjh.devtoon.member.dto.response.MemberResponse;

@RequestMapping("/v1/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    /**
     * 웹툰 구독자 회원 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid final MemberRegisterRequest request
    ) {
        memberService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 웹툰 구독자 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieve(
            @PathVariable final Long id
    ) {
        MemberEntity member = memberService.retrieve(id);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 웹툰 구독자 회원 등급 변경
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> changeMembershipStatus(
            @PathVariable final Long id,
            @RequestBody @Valid final MembershipStatusChangeRequest request
    ) {
        memberService.changeMembershipStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> retrieveMyInfo(Authentication authentication) {
        // 사용자 이메일이 저장된다.(security에서는 name이라고 칭함)
        String memberEmail = authentication.getName();

        MemberEntity member = memberService.retrieveMyInfo(memberEmail);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
