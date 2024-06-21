package yjh.devtoon.member.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorMessage {

    private static final String ID_NOT_FOUND = "id : '%d' 를 찾을 수 없습니다.";
    private static final String MEMBERSHIP_STATUS_NOT_FOUND = "membership status : '%s' 를 찾을 수 없습니다.";
    private static final String EMAIL_CONFLICT = "email : '%s' 가 존재합니다.";

    public static String getMemberNotFound(final Long id) {
        return String.format(ID_NOT_FOUND, id);
    }

    public static String getMembershipStatusNotFound(final String status) {
        return String.format(MEMBERSHIP_STATUS_NOT_FOUND, status);
    }

    public static String getEmailConflict(final String email) {
        return String.format(EMAIL_CONFLICT, email);
    }

}
