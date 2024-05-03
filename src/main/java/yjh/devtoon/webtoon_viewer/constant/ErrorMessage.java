package yjh.devtoon.webtoon_viewer.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorMessage {

    public static final String EMAIL_CONFLICT = "email : '%s' 가 존재합니다.";

    public static String EMAIL_CONFLICT(final String email) {
        return String.format(EMAIL_CONFLICT, email);
    }

}
