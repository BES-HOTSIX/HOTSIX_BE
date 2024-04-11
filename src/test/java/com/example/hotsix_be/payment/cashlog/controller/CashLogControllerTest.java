package com.example.hotsix_be.payment.cashlog.controller;

import com.example.hotsix_be.common.controller.DefaultControllerTest;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashLogController.class)
public class CashLogControllerTest extends DefaultControllerTest {
    private final String baseUrl = "/api/v1/cashLog";

    @MockBean
    private CashLogService cashLogService;

    private ResultActions performShowMyCashLogs() throws Exception {
        return mockMvc.perform(get(baseUrl + "/me")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    private ResultActions performShowConfirm(final Long cashLogId) throws Exception {
        return mockMvc.perform(get(baseUrl + "/{cashLogId}/confirm", cashLogId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    @Test
    @DisplayName("내 캐시 내역 페이지를 조회한다.")
    void showMyCashLogs() throws Exception {
        // given
        when(cashLogService.findMyPageList(any(), any())).thenReturn(null);

        // when
        final ResultActions resultActions = performShowMyCashLogs();

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("예약 결제, 예약 취소 확인 페이지를 조회한다.")
    void showConfirm() throws Exception {
        // given
        CashLog cashLog = CashLog.builder()
                .eventType(EventType.결제__예치금)
                .amount(10000L)
                .build();

        when(cashLogService.getConfirmRespById(any(), any())).thenReturn(ConfirmResponse.of(
                null, null, null, null, null, null, null, "결제"));

        // when
        final ResultActions resultActions = performShowConfirm(1L);

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}