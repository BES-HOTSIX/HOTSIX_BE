package com.example.hotsix_be.common.controller;

import com.example.hotsix_be.auth.MemberOnlyChecker;
import com.example.hotsix_be.common.config.SecurityConfig;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@MockBean(JpaMetamodelMappingContext.class)
@Import({
        MemberOnlyChecker.class,
        BearerAuthorizationExtractor.class,
        SecurityConfig.class
})
public abstract class DefaultControllerTest {
    protected static final MemberTokens MEMBER_TOKENS = new MemberTokens("refreshToken", "Bearer accessToken");
    protected static final Cookie COOKIE = new Cookie("refresh-token", MEMBER_TOKENS.getRefreshToken());

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(refreshTokenRepository.existsById(any())).willReturn(true);
        doNothing().when(jwtProvider).validateTokens(any());
        given(jwtProvider.getSubject(any())).willReturn("1");
    }

    //    private ResultActions perform() throws Exception {
//        return mockMvc.perform(
//                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
//                .cookie(COOKIE)
//                .contentType(APPLICATION_JSON));
//    }
}
