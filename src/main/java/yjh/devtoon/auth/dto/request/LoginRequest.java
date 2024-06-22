package yjh.devtoon.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "email을 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 3, max = 50)
    private String email;

    @NotBlank(message = "password를 확인해주세요. 빈값 혹은 null 일 수 없습니다.")
    @Size(min = 3, max = 100)
    private String password;

    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
