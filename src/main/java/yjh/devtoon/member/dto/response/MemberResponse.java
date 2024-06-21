package yjh.devtoon.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yjh.devtoon.member.domain.MembershipStatus;
import yjh.devtoon.member.domain.MemberEntity;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class MemberResponse {

    private final String name;
    private final String email;
    private final MembershipStatus membershipStatus;
    private final LocalDateTime createdAt;

    public static MemberResponse from(final MemberEntity member) {
        return new MemberResponse(
                member.getName(),
                member.getEmail(),
                member.getMembershipStatus(),
                member.getCreatedAt()
        );
    }

}
