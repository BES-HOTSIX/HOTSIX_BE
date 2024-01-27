package com.example.hotsix_be.login.service;

import com.example.hotsix_be.login.dto.google.GoogleTokenResponseDto;
import com.example.hotsix_be.login.dto.kakao.KakaoTokenResponseDto;
import com.example.hotsix_be.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GoogleOauthService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private MemberRepository memberRepository;

    private static final String PROPERTIES_PATH = "${oauth2.provider.google.";
    private static final String PROVIDER_NAME = "google";

    private static final String GRANT_TYPE = "authorization_code";

    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;
    protected final String tokenUri;
    protected final String userUri;

    public GoogleOauthService(
            @Value(PROPERTIES_PATH + "client-id}") final String clientId,
            @Value(PROPERTIES_PATH + "client-secret}") final String clientSecret,
            @Value(PROPERTIES_PATH + "redirect-uri}") final String redirectUri,
            @Value(PROPERTIES_PATH + "token-uri}") final String tokenUri,
            @Value(PROPERTIES_PATH + "user-info}") final String userUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
    }


    public Mono<GoogleTokenResponseDto> getToken(String code) {
        String uri = tokenUri + "?grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&client_secret" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GoogleTokenResponseDto.class);
    }

}