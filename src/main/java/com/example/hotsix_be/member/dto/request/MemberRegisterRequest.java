package com.example.hotsix_be.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequest {

    private String username;
    private String password;
    private String nickname;
}
