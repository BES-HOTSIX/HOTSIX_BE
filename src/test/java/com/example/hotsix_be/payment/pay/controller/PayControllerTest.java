package com.example.hotsix_be.payment.pay.controller;

import com.example.hotsix_be.common.controller.DefaultControllerTest;
import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.example.hotsix_be.payment.pay.service.PayService;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.hotsix_be.coupon.entity.CouponType.신규회원;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PayController.class)
public class PayControllerTest extends DefaultControllerTest {
    private final String baseUrl = "/api/v1/pay";

    @MockBean
    private CashLogService cashLogService;

    @MockBean
    private PayService payService;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private CouponService couponService;

    private ResultActions performShowPay(final Long reserveId) throws Exception {
        return mockMvc.perform(get(baseUrl + "/{reserveId}", reserveId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    private ResultActions performPayByCash(final Long reserveId, final UseCouponRequest useCouponRequest) throws Exception {
        return mockMvc.perform(post(baseUrl + "/{reserveId}/byCash", reserveId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .content(objectMapper.writeValueAsString(useCouponRequest))
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performPayByToss(final Long reserveId, final TossConfirmRequest tossConfirmRequest) throws Exception {
        return mockMvc.perform(post(baseUrl + "/{reserveId}/byToss", reserveId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .content(objectMapper.writeValueAsString(tossConfirmRequest))
                .contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("결제하기 화면을 조회한다")
    void showPay() throws Exception {
        // given
        when(reservationService.getUnpaidDetailById(any(), any())).thenReturn(null);

        // when
        final ResultActions resultActions = performShowPay(1L);

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("보유하고 있는 캐시만을 통해 결제합니다.")
    void payByCash() throws Exception {
        // given
        when(payService.payByCashOnly(any(), any())).thenReturn(Pay.builder().id(1L).build());
        when(cashLogService.getCashLogIdById(any())).thenReturn(null);

        // when
        final ResultActions resultActions = performPayByCash(1L, new UseCouponRequest(null, 30_000L));

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("토스페이먼츠만을 통해 잔액을 모두 결제하거나 사용하고자 하는 캐시를 차감하고 남은 금액을 결제합니다.")
    void payByToss() throws Exception {
        // given
        when(payService.payByTossPayments(any(), any(), any())).thenReturn(Pay.builder().id(1L).build());

        // when
        final ResultActions resultActions = performPayByToss(1L, new TossConfirmRequest("NORMAL", "a4CWyWY5m89PNh7xJwhk1", "100000", "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6", 0L, 신규회원 ));

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
