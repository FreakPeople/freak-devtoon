package yjh.devtoon.viewer.dto.request;

import lombok.Getter;

@Getter
public class WebtoonViewerRegisterRequest {

    private final String name;
    private final String email;
    private final String password;

    public WebtoonViewerRegisterRequest(
            final String name,
            final String email,
            final String password
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
