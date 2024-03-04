package com.example.hotsix_be.member.dto.response;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberInfoResponse {

    private final String username;
    private final String nickname;
    private final String imageUrl;
    private final Long restCash;
    private final Role role;

    public static MemberInfoResponse of(final Member member) {
        return new MemberInfoResponse(
                member.getUsername(),
                member.getNickname(),
                member.getImageUrl(),
                member.getRestCash(),
                member.getRole()
        );
    }
}
