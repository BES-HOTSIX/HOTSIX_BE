package com.example.hotsix_be.payment.refund.controller;

import com.example.hotsix_be.common.controller.DefaultControllerTest;
import com.example.hotsix_be.coupon.dto.request.DiscountAmountRequest;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.refund.entity.Refund;
import com.example.hotsix_be.payment.refund.service.RefundService;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RefundController.class)
public class RefundControllerTest extends DefaultControllerTest {
    private final String baseUrl = "/api/v1/refund";

    @MockBean
    private RefundService refundService;

    @MockBean
    private CashLogService cashLogService;

    @MockBean
    private ReservationService reservationService;

    private ResultActions performCancelReservation(final Long reserveId, final DiscountAmountRequest discountAmountRequest) throws Exception {
        return mockMvc.perform(patch(baseUrl + "/reserve/{reserveId}", reserveId)
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .content(objectMapper.writeValueAsString(discountAmountRequest))
                .contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("예약을 취소한다.")
    void cancelReservation() throws Exception {
        // given
        Long reserveId = 1L;
        DiscountAmountRequest discountAmountRequest = new DiscountAmountRequest(10000L);
        Member member = Member.builder().id(1L).build();
        Reservation reservation = Reservation.builder().member(member).checkInDate(LocalDate.now().plusDays(1)).build();
        Refund refund = Refund.builder().id(1L).build();

        when(reservationService.findPaidById(any())).thenReturn(reservation);
        when(refundService.doRefund(any(),any())).thenReturn(refund);

        // when
        final ResultActions resultActions = performCancelReservation(reserveId, discountAmountRequest);

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
