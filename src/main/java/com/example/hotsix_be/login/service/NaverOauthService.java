package com.example.hotsix_be.login.service;

import static com.example.hotsix_be.member.entity.SocialProvider.NAVER;

import com.example.hotsix_be.login.dto.naver.Response;
import com.example.hotsix_be.login.dto.naver.NaverTokenResponseDto;
import com.example.hotsix_be.login.dto.naver.NaverUserInfoDto;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NaverOauthService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private MemberRepository memberRepository;

    private static final String PROPERTIES_PATH = "${oauth2.provider.naver.";
    private static final String GRANT_TYPE = "authorization_code";

    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;
    protected final String tokenUri;
    protected final String userUri;

    public NaverOauthService(
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

    public Mono<NaverTokenResponseDto> getToken(String code, String state) {
        String uri =
                tokenUri + "?grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&client_secret=" + clientSecret
                        + "&redirect_uri=" + redirectUri
                        + "&code=" + code + "&state=" + state;

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NaverTokenResponseDto.class);
    }

    public Mono<NaverUserInfoDto> getMemberInfo(String token) {

        return webClient.get()
                .uri(userUri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(NaverUserInfoDto.class)
                .next(); // Flux 스트림의 첫 번째 항목을 반환
    }

    @Transactional
    public Member registerMember(Response response) {
        String nickname = response.getNickname();
        String profileImageUrl = response.getProfile_image();

        Optional<Member> oauthMember = memberRepository.findMemberByNicknameAndSocialProvider(nickname,
                NAVER);

        if (oauthMember.isPresent()) {
            return oauthMember.get();
        } else {
            Member member = new Member(nickname + "1", profileImageUrl, NAVER);
            return memberRepository.save(member);
        }
    }
}