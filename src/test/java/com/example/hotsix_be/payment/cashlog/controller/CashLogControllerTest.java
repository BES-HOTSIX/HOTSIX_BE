package com.example.hotsix_be.payment.cashlog.controller;

import com.example.hotsix_be.login.LoginArgumentResolver;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashLogController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
public class CashLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginArgumentResolver argumentResolver;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private BearerAuthorizationExtractor bearerExtractor;

    private static final MemberTokens MEMBER_TOKENS = new MemberTokens("refreshToken", "accessToken");
    private static final Cookie COOKIE = new Cookie("refresh-token", MEMBER_TOKENS.getRefreshToken());

    private final String baseUrl = "/api/v1/cashLog";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CashLogService cashLogService;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        given(refreshTokenRepository.existsById(any())).willReturn(true);
        doNothing().when(jwtProvider).validateTokens(any());
        given(jwtProvider.getSubject(any())).willReturn("1");
    }

    private ResultActions performShowMyCashLogs() throws Exception {
        return mockMvc.perform(get(baseUrl + "/me")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("내 캐시 내역 페이지를 조회한다.")
    void showMyCashLogs() throws Exception {
        // given
        when(cashLogService.findMyPageList(any(), any())).thenReturn(MyCashLogResponse.of("test", 1000000L, "test@gmail.com", null));

        // when
        final ResultActions resultActions = performShowMyCashLogs();

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
