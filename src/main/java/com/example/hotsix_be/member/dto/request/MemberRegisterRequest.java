package com.example.hotsix_be.member.dto.request;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "회원 가입을 위한 입력 폼")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class MemberRegisterRequest {

    @Schema(description = "아이디", example = "test51")
    @NotBlank
    private String username;

    @Schema(description = "비밀번호", example = "test51")
    @NotBlank
    private String password;

    @Schema(description = "닉네임", example = "테스트51")
    @NotBlank
    private String nickname;

    private String email;

    private String imageUrl;
}
