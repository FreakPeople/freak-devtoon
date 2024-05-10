package yjh.devtoon.common.response;

import lombok.Getter;

@Getter
public class ApiReponse<T> {

    private final String statusMessage;
    private final T data;

    public ApiReponse(
            final String statusMessage,
            final T data) {
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> ApiReponse<T> success(final T data) {
        return new ApiReponse<>(
                "성공",
                data
        );
    }

    public static <T> ApiReponse<T> error(final T data) {
        return new ApiReponse<>(
                "실패",
                data
        );
    }

}
