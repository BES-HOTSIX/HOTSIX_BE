package com.example.hotsix_be.login.dto.google;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class GoogleUserInfoDto {
    private String sub; // 고유 식별자
    private String name; // 전체 이름
    private String given_name; // 주어진 이름 (이름)
    private String family_name; // 가족 이름 (성)
    private String picture; // 프로필 이미지 URL
    private String email; // 이메일 주소
    private Boolean email_verified; // 이메일 인증 여부
    private String locale; // 사용자 로케일 (언어 설정)
}