package com.example.hotsix_be.login.dto.kakao;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class KakaoUserInfo {

    private Long id;

    private String connected_at;

    private KakaoProperties properties;

    private Map<String, Object> kakao_account;
}
