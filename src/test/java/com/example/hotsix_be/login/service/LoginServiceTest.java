
package com.example.hotsix_be.login.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.domain.RefreshToken;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.response.LoginResponse;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(MockitoExtension.class)
@Transactional
class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private LoginService loginService;


    @Test
    void 로그인_성공() {
        // given
        LoginRequest loginRequest = new LoginRequest("username", "password");
        // Member 객체 생성 시 passwordEncoder.encode("password")로 비밀번호 인코딩
        Member member = new Member("username", passwordEncoder.encode("password"), "user1",
                "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "test@test.com");

        member = member.toBuilder()
                .id(1L)
                .build();

        MemberTokens memberTokens = new MemberTokens("dummyRefreshToken", "dummyAccessToken");
        given(jwtProvider.generateLoginToken(member.getId().toString())).willReturn(memberTokens);
        given(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())).willReturn(true);

        RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(), member.getId());
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(savedRefreshToken);

        // when
        LoginResponse result = loginService.login(loginRequest, member);

        // then
        assertThat(result.getAccessToken()).isEqualTo(memberTokens.getAccessToken());
        assertThat(result.getRefreshToken()).isEqualTo(memberTokens.getRefreshToken());
    }

    @Test
    void 로그인_실패_비밀번호_불일치() {
        // given
        String username = "username";
        String wrongPassword = "wrongPassword";
        Member member = new Member("username", passwordEncoder.encode("password"), "user1",
                "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "test@test.com");

        LoginRequest loginRequest = new LoginRequest(username, wrongPassword);

        when(passwordEncoder.matches(wrongPassword, member.getPassword())).thenReturn(false);

        // when & then
        assertThrows(AuthException.class, () -> loginService.login(loginRequest, member));
    }

}

