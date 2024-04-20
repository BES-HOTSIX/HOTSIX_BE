package com.example.hotsix_be.payment.pay.service;

import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.repository.PayRepository;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import com.example.hotsix_be.reservation.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.INSUFFICIENT_DEPOSIT;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.coupon.entity.CouponType.신규회원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {
    @InjectMocks
    private PayService payService;

    @Mock
    private PayRepository payRepository;

    @Mock
    private CashLogService cashLogService;

    @Mock
    private TossService tossService;

    @Mock
    private RechargeService rechargeService;

    @Mock
    private CouponService couponService;

    private Member guest;
    private Reservation reservation;

    @BeforeEach
    void initData() {
        Member host = Member.builder()
                .id(2L)
                .socialLoginId("techit")
                .nickname("techit")
                .imageUrl(null)
                .build();
        host.assignRole(Role.HOST);

        guest = Member.builder()
                .id(3L)
                .socialLoginId("Park")
                .nickname("Park")
                .imageUrl(null)
                .restCash(0L)
                .build();
        guest.assignRole(Role.GUEST);

        Hotel hotel = Hotel.builder()
                .hotelType("호텔")
                .address("서울시 강남구")
                .addressDetail("강남역 1번출구")
                .roomCnt(10L)
                .bedCnt(10L)
                .bathroomCnt(10L)
                .maxPeople(10L)
                .facility(List.of("헬스장"))
                .nickname("강남호텔")
                .description("강남호텔입니다.")
                .price(10000L)
                .owner(host)
                .build();

        reservation = Reservation.builder()
                .checkInDate(LocalDate.now().plusDays(3))
                .checkOutDate(LocalDate.now().plusDays(6))
                .guests(2L)
                .price(100000L)
                .isPaid(false)
                .hotel(hotel)
                .member(guest)
                .build();
    }

    @Test
    @DisplayName("포인트 결제를 진행한다")
    void payByCashOnly() {
        // given
        guest.addCash(200000L, 0L);
        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 10000L);

        // when
        payService.payByCashOnly(reservation, useCouponRequest);

        // then
        assertThat(reservation.getOrderId()).startsWith("o");
        assertThat(reservation.isPaid()).isTrue();
    }

    @Test
    @DisplayName("포인트 결제를 진행한다 - 실패 케이스(잔액 부족)")
    void payByCashOnly_insufficientDeposit() {
        // given
        guest.addCash(50000L, 0L);
        System.out.println(guest.getRestCash());
        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 0L);

        // when
        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(INSUFFICIENT_DEPOSIT.getMessage());
    }

    @Test
    @DisplayName("포인트 결제를 진행한다 - 실패 케이스(중복 결제)")
    void payByCashOnly_invalidRequest() {
        // given
        guest.addCash(500000L, 0L);
        System.out.println(guest.getRestCash());
        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 0L);

        // when
        payService.payByCashOnly(reservation, useCouponRequest);
        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_REQUEST.getMessage());
    }


    @Test
    @DisplayName("복합 혹은 토스페이먼츠 결제를 진행한다")
    void payByTossPayments() {
        // given
        guest.addCash(50000L, 0L);
        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 10000L);
        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest("NORMAL", "oa4CWyWY5m89PNh7xJwhk1", "100000", "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6", 0L, 신규회원);
        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest("o8sJILLP1EP6V1nLksCBL", 100000L, "간편결제", "DONE", "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ", "20", null);

        when(tossService.confirmTossPayment(any())).thenReturn(Mono.just(tossPaymentRequest));

        // when
        payService.payByTossPayments(tossConfirmRequest, reservation, 0L);

        // then
        assertThat(reservation.getOrderId()).startsWith("o");
        assertThat(reservation.isPaid()).isTrue();
    }

    @Test
    @DisplayName("복합 혹은 토스페이먼츠 결제를 진행한다 - 실패 케이스(잘못된 요청)")
    void payByTossPayments_invalidRequest() {
        // given
        guest.addCash(0L, 0L);
        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest("NORMAL", "oa4CWyWY5m89PNh7xJwhk1", "50000", "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6", 0L, 신규회원);

        // when
        Throwable thrown = catchThrowable(() -> payService.payByTossPayments(tossConfirmRequest, reservation, 0L));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_REQUEST.getMessage());
    }
}
