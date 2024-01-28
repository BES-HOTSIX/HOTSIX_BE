package com.example.hotsix_be.login.service;

import static com.example.hotsix_be.member.entity.SocialProvider.GOOGLE;

import com.example.hotsix_be.login.dto.google.GoogleTokenResponseDto;
import com.example.hotsix_be.login.dto.google.GoogleUserInfoDto;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
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
        return webClient.post()
                .uri(tokenUri)
                .body(BodyInserters
                        .fromFormData("grant_type", GRANT_TYPE)
                        .with("code", code)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(GoogleTokenResponseDto.class);
    }

    public Mono<GoogleUserInfoDto> getUserInfo(String token) {
        return webClient.get()
                .uri(userUri) // Google 사용자 정보 URI
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(GoogleUserInfoDto.class);
    }

    @Transactional
    public Member registerGoogleMember(GoogleUserInfoDto googleUserInfoDto) {
        String nickname = googleUserInfoDto.getName();
        String profileImageUrl = googleUserInfoDto.getPicture(); // Google 응답에 맞는 필드명으로 조정

        Optional<Member> oauthMember = memberRepository.findByNicknameAndSocialProvider(nickname, GOOGLE);

        if (oauthMember.isPresent()) {
            return oauthMember.get();
        } else {
            Member member = new Member(nickname, profileImageUrl, GOOGLE); // Google을 위한 Enum이나 상수
            return memberRepository.save(member);
        }
    }

}