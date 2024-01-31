package com.example.hotsix_be.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPasswordChangeRequest {
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank
    private String password;
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @NotBlank
    private String passwordCheck;
}
