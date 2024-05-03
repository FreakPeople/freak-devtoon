package yjh.devtoon.webtoon_viewer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MembershipStatusChangeRequest {

    @NotBlank(message = "status를 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    private String membershipStatus;

    public MembershipStatusChangeRequest() {
    }

}