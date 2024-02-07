package com.example.hotsix_be.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 변경을 위한 입력 폼")
public class MemberPasswordChangeRequest {
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank
    @Schema(description = "변경할 비밀번호")
    private String password;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank
    @Schema(description = "변경할 비밀번호 확인")
    private String passwordCheck;
}
