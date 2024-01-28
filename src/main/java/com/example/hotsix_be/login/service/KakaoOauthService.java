package com.example.hotsix_be.login.service;


import static com.example.hotsix_be.member.entity.SocialProvider.*;

import com.example.hotsix_be.login.dto.kakao.KakaoPropertiesDto;
import com.example.hotsix_be.login.dto.kakao.KakaoTokenResponseDto;
import com.example.hotsix_be.login.dto.kakao.KakaoUserInfoDto;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.SocialProvider;
import com.example.hotsix_be.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoOauthService {


    @Autowired
    private WebClient webClient;

    @Autowired
    private MemberRepository memberRepository;

    private static final String PROPERTIES_PATH = "${oauth2.provider.kakao.";
    private static final String PROVIDER_NAME = "kakao";
    private static final String SECURE_RESOURCE = "secure_resource";

    private static final String GRANT_TYPE = "authorization_code";

    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;
    protected final String tokenUri;
    protected final String userUri;

    public KakaoOauthService(
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


    public Mono<KakaoTokenResponseDto> getToken(String code) {
        String uri = tokenUri + "?grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&client_secret" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class);

    }

    public Mono<KakaoUserInfoDto> getMemberInfo(String token) {

        return webClient.get()
                .uri(userUri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(KakaoUserInfoDto.class)
                .next(); // Flux 스트림의 첫 번째 항목을 반환
    }

    @Transactional
    public Member registerMember(KakaoPropertiesDto properties) {
        String nickname = properties.getNickname();
        String profileImageUrl = properties.getProfile_image();

        Optional<Member> oauthMember = memberRepository.findMemberByNicknameAndSocialProvider(nickname,
                KAKAO);

        if (oauthMember.isPresent()) {
            return oauthMember.get();
        } else {
            Member member = new Member(nickname, profileImageUrl, KAKAO);
            return memberRepository.save(member);
        }
    }
}
