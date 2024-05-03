package yjh.devtoon.webtoon_viewer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WebtoonViewerRegisterRequest {

    @NotBlank(message = "name을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 1, max = 20)
    private final String name;

    @NotBlank(message = "email을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 1, max = 20)
    private final String email;

    @NotBlank(message = "password을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 5, max = 20)
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
