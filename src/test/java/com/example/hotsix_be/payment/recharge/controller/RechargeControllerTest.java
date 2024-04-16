package com.example.hotsix_be.payment.recharge.controller;

import com.example.hotsix_be.common.controller.DefaultControllerTest;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossWebhookRequest;
import com.example.hotsix_be.payment.payment.service.TossServiceImpl;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RechargeController.class)
public class RechargeControllerTest extends DefaultControllerTest {
    private final String baseUrl = "/api/v1/recharge";

    @MockBean
    private RechargeService rechargeService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private TossServiceImpl tossServiceImpl;

    private ResultActions performShowMyRecharge() throws Exception {
        return mockMvc.perform(get(baseUrl + "/me")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    private ResultActions performRecharge(final TossConfirmRequest tossConfirmRequest) throws Exception {
        return mockMvc.perform(post(baseUrl + "/request")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .content(objectMapper.writeValueAsString(tossConfirmRequest))
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performTossWebhook(final TossWebhookRequest tossWebhookRequest) throws Exception {
        return mockMvc.perform(post(baseUrl + "/tossWebhook")
                .content(objectMapper.writeValueAsString(tossWebhookRequest))
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performCancelRecharge(final String orderId) throws Exception {
        return mockMvc.perform(patch(baseUrl + "/{orderId}/cancel", orderId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    @Test
    @DisplayName("내 충전 내역을 조회한다.")
    void showMyRecharge() throws Exception {
        // given

        // when
        final ResultActions resultActions = performShowMyRecharge();

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("충전 신청을 한다.")
    void recharge() throws Exception {
        // given

        // when
        final ResultActions resultActions = performRecharge(new TossConfirmRequest(null, null, null, null, null, null));

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("무통장 입금을 위한 웹훅 요청을 받는다.")
    void tossWebhook() throws Exception {
        // given
        String secret = "secret";
        String orderId = "orderId";
        when(rechargeService.findByOrderId(any())).thenReturn(Recharge.builder().secret(secret).build());

        // when
        final ResultActions resultActions = performTossWebhook(new TossWebhookRequest(orderId, null, secret));

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("무통장 입금 신청을 취소한다.")
    void cancelRecharge() throws Exception {
        // given
        String orderId = "orderId";

        // when
        final ResultActions resultActions = performCancelRecharge(orderId);

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
