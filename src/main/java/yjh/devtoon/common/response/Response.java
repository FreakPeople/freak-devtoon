package yjh.devtoon.common.response;

import lombok.Getter;

@Getter
public class Response<T> {

    private final String statusMessage;
    private final T data;

    public Response(
            final String statusMessage,
            final T data) {
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> Response<T> success(final T data) {
        return new Response<>(
                "성공",
                data
        );
    }

    public static <T> Response<T> error(final T data) {
        return new Response<>(
                "실패",
                data
        );
    }

}
