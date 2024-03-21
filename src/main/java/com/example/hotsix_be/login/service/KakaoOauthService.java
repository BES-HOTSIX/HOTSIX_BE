package com.example.hotsix_be.login.service;

import com.example.hotsix_be.login.dto.kakao.KakaoProperties;
import com.example.hotsix_be.login.dto.kakao.KakaoTokenResponse;
import com.example.hotsix_be.login.dto.kakao.KakaoUserInfo;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.hotsix_be.member.entity.SocialProvider.KAKAO;

@Component
@Slf4j
public class KakaoOauthService {


    private static final String PROPERTIES_PATH = "${oauth2.provider.kakao.";

    private static final String GRANT_TYPE = "authorization_code";

    private final WebClient webClient;
    private final MemberRepository memberRepository;
    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;
    protected final String tokenUri;
    protected final String userUri;

    public KakaoOauthService(
            final WebClient webClient,
            final MemberRepository memberRepository,
            @Value(PROPERTIES_PATH + "client-id}") final String clientId,
            @Value(PROPERTIES_PATH + "client-secret}") final String clientSecret,
            @Value(PROPERTIES_PATH + "redirect-uri}") final String redirectUri,
            @Value(PROPERTIES_PATH + "token-uri}") final String tokenUri,
            @Value(PROPERTIES_PATH + "user-info}") final String userUri
    ) {
        this.webClient = webClient;
        this.memberRepository = memberRepository;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
    }


    public Mono<KakaoTokenResponse> getToken(final String code) {
        String uri =
                tokenUri + "?grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&client_secret=" + clientSecret
                        + "&redirect_uri=" + redirectUri
                        + "&code=" + code;

        log.info("code = {}", code);
        log.info("tokenUri = {}", tokenUri);
        log.info("clientId = {}", clientId);
        log.info("clientSecret = {}", clientSecret);
        log.info("redirectUri = {}", redirectUri);

        return webClient.post()  // HTTP POST 메소드 사용
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)  // 컨텐츠 타입 설정
                .body(BodyInserters
                        .fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class);
    }

    public Mono<KakaoUserInfo> getMemberInfo(final String token) {

        return webClient.get()
                .uri(userUri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(KakaoUserInfo.class)
                .next(); // Flux 스트림의 첫 번째 항목을 반환
    }

    @Transactional
    public Member registerMember(final KakaoProperties properties) {
        String nickname = properties.getNickname();
        String profileImageUrl = properties.getProfile_image();

        Optional<Member> oauthMember = memberRepository.findByNicknameAndSocialProvider(nickname,
                KAKAO);

        if (oauthMember.isPresent()) {
            return oauthMember.get();
        } else {
            Member member = new Member(nickname, profileImageUrl, KAKAO);
            return memberRepository.save(member);
        }
    }
}
