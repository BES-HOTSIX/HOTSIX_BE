package com.example.hotsix_be.member.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberInfoResponse {

    private final String username;
    private final String nickname;
    private final String imageUrl;

    public static MemberInfoResponse of(final Member member) {
        return new MemberInfoResponse(
                member.getUsername(),
                member.getNickname(),
                member.getImageUrl()
        );
    }
}
