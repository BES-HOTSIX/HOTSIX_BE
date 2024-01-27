package com.example.hotsix_be.login.dto.kakao;

import java.util.Map;
import lombok.Data;

@Data
public class KakaoUserInfoDto {

    private Long id;

    private String connected_at;

    private KakaoPropertiesDto properties;

    private Map<String, Object> kakao_account;
}
