package com.example.hotsix_be.payment.recharge.service;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.dto.request.VirtualAccountRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.dto.response.RechargePageResponse;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.repository.RechargeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RechargeServiceTest {
    @InjectMocks
    private RechargeService rechargeService;

    @Mock
    private RechargeRepository rechargeRepository;

    @Mock
    private CashLogService cashLogService;

    @Mock
    private MemberService memberService;

    @Mock
    private TossService tossService;

    @Test
    @DisplayName("가상계좌 충전 신청을 진행합니다.")
    void doRecharge_virtual() {
        // given
        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest(
                "o8sJILLP1EP6V1nLksCBL",
                50000L,
                "가상계좌",
                "WAITING_FOR_DEPOSOT",
                "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ",
                "20",
                new VirtualAccountRequest("12312345612345", "홍길동")
        );

        when(tossService.confirmTossPayment(any())).thenReturn(Mono.just(tossPaymentRequest));

        // when
        rechargeService.doRecharge(null, Accessor.member(1L), null);

        // then
    }

    @Test
    @DisplayName("")
    void doRecharge_easyPay() {
        // given
        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest(
                "o8sJILLP1EP6V1nLksCBL",
                50000L,
                "간편결제",
                "DONE",
                null,
                null,
                null
        );

        when(tossService.confirmTossPayment(any())).thenReturn(Mono.just(tossPaymentRequest));

        // when
        rechargeService.doRecharge(null, Accessor.member(1L), null);

        // then
    }

    @Test
    @DisplayName("")
    void doRecharge_invalidRequest() {
        // given
        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest(
                "o8sJILLP1EP6V1nLksCBL",
                50000L,
                "지원하지 않는 결제",
                "DONE",
                null,
                null,
                null
        );

        when(tossService.confirmTossPayment(any())).thenReturn(Mono.just(tossPaymentRequest));

        // when
        Throwable thrown = catchThrowable(() -> rechargeService.doRecharge(null, Accessor.member(1L), null));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("충전을 진행한다 - 실패 케이스(이미 취소된 충전 신청)")
    void processRecharge_paymentNotPossible() {
        // given
        Recharge recharge = Recharge.builder().cancelDate(LocalDateTime.now()).build();

        // when
        Throwable thrown = catchThrowable(() -> rechargeService.processRecharge(recharge, null));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(PAYMENT_NOT_POSSIBLE.getMessage());
    }

    @Test
    @DisplayName("충전 신청을 취소한다.")
    void cancelRecharge() {
        // given
        Recharge recharge = Recharge.builder().build();

        // when
        rechargeService.cancelRecharge(recharge);

        // then
        assertThat(recharge.getCancelDate()).isNotNull();
    }

    @Test
    @DisplayName("충전 신청을 취소한다. - 실패 케이스(이미 결제된 충전 신청)")
    void cancelRecharge_cancellationNotPossible() {
        // given
        Recharge recharge = Recharge.builder().payDate(LocalDateTime.now()).build();

        // when
        Throwable thrown = catchThrowable(() -> rechargeService.cancelRecharge(recharge));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(CANCELLATION_NOT_POSSIBLE.getMessage());
    }

    @Test
    @DisplayName("OrderId 를 통해 Recharge 를 조회한다.")
    void findByOrderId() {
        // given
        when(rechargeRepository.findByOrderId(any())).thenReturn(Optional.ofNullable(Recharge.builder().build()));

        // when
        rechargeService.findByOrderId("ABCDE");

        // then
    }

    @Test
    @DisplayName("OrderId 를 통해 Recharge 를 조회한다. - 실패 케이스(존재하지 않는 충전 신청)")
    void findByOrderId_notFoundRechargeId() {
        // given
        when(rechargeRepository.findByOrderId(any())).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> rechargeService.findByOrderId("ABCD"));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(NOT_FOUND_RECHARGE_ID.getMessage());
    }

    @Test
    @DisplayName("OrderId와 MemberId를 통해 Recharge를 조회한다.")
    void findByOrderIdAndMemberId() {
        // given
        when(rechargeRepository.findByOrderIdContainingAndMember(any(),any())).thenReturn(Optional.ofNullable(Recharge.builder().build()));

        // when
        rechargeService.findByOrderIdAndMemberId("ABCDE", 1L);

        // then
    }

    @Test
    @DisplayName("OrderId와 MemberId를 통해 Recharge를 조회한다. - 실패 케이스(존재하지 않는 충전 신청)")
    void findByOrderIdAndMemberId_notFoundRechargeId() {
        // given
        when(rechargeRepository.findByOrderIdContainingAndMember(any(),any())).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> rechargeService.findByOrderIdAndMemberId("ABCDE", 1L));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(NOT_FOUND_RECHARGE_ID.getMessage());
    }

    @Test
    @DisplayName("나의 Recharge 들을 페이지 형식으로 반환한다. ")
    void getRechargePageResponse() {
        // given
        Accessor accessor = Accessor.member(1L);
        Pageable pageable = PageRequest.of(0, 1);
        List<Recharge> rechargeList = new ArrayList<>();
        when(rechargeRepository.findAllByMember(any(), any())).thenReturn(new PageImpl<Recharge>(rechargeList, pageable, 0));

        // when
        Page<RechargePageResponse> result = rechargeService.getRechargePageResponse(accessor, pageable);

        // then
        assertThat(result).isNotNull();
    }
}
