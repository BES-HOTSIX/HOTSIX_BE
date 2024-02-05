package com.example.hotsix_be.login.dto.request;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인을 위한 회원 정보 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class LoginRequest {

    @Schema(description = "아이디", example = "test1")
    @NotBlank(message = "아이디는 비어 있을 수 없습니다.")
    private String username;

    @Schema(description = "비밀번호", example = "test1")
    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    private String password;

}
