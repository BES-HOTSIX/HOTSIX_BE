//package com.example.hotsix_be.payment.pay.service;
//
//import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
//import com.example.hotsix_be.coupon.service.CouponService;
//import com.example.hotsix_be.hotel.entity.Hotel;
//import com.example.hotsix_be.member.entity.Member;
//import com.example.hotsix_be.member.entity.Role;
//import com.example.hotsix_be.payment.cashlog.service.CashLogService;
//import com.example.hotsix_be.payment.pay.repository.PayRepository;
//import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
//import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
//import com.example.hotsix_be.payment.payment.exception.PaymentException;
//import com.example.hotsix_be.payment.payment.service.TossService;
//import com.example.hotsix_be.payment.recharge.service.RechargeService;
//import com.example.hotsix_be.reservation.entity.Reservation;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static com.example.hotsix_be.coupon.entity.CouponType.신규회원;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchThrowable;
//
//@ExtendWith(MockitoExtension.class)
//@RequiredArgsConstructor
//public class PayServiceTest {
//    @InjectMocks
//    private PayService payService;
//
//    @Mock
//    private PayRepository payRepository;
//
//    @Spy
//    @InjectMocks
//    private CashLogService cashLogService;
//
//    @Mock
//    private RechargeService rechargeService;
//
//    private final TossService tossService;
//
//    @Mock
//    private CouponService couponService;
//
//    private Member guest;
//    private Member host;
//    private Hotel hotel;
//    private Reservation reservation;
//
//    @BeforeEach
//    void initData() {
//        guest = new Member(
//                1L,
//                "KIM",
//                "KIM",
//                null
//        );
//        guest.assignRole(Role.GUEST);
//
//        host = new Member(
//                2L,
//                "techit",
//                "techit",
//                null
//        );
//        host.assignRole(Role.HOST);
//        hotel = new Hotel(
//                "호텔",
//                "서울시 강남구",
//                "강남역 1번출구",
//                10L,
//                10L,
//                10L,
//                10L,
//                List.of("헬스장"),
//                "강남호텔",
//                "강남호텔입니다.",
//                10000L,
//                host
//        );
//        reservation = new Reservation(
//                LocalDate.now().plusDays(3),
//                LocalDate.now().plusDays(6),
//                2L,
//                100000L,
//                false,
//                hotel,
//                guest
//        );
//    }
//
//    @Test
//    @DisplayName("포인트 결제를 진행한다")
//    void payByCashOnly() {
//        // given
//        guest.addCash(200000L, 0L);
//        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 10000L);
//
//        // when
//        payService.payByCashOnly(reservation, useCouponRequest);
//
//        // then
//        assertThat(reservation.getOrderId()).startsWith("o");
//        assertThat(reservation.isPaid()).isTrue();
//    }
//
//    @Test
//    @DisplayName("포인트 결제를 진행한다 - 실패 케이스(잔액 부족)")
//    void payByCashOnlyInsufficientDeposit() {
//        // given
//        guest.addCash(50000L, 0L);
//        System.out.println(guest.getRestCash());
//        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 0L);
//
//        // when
//        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));
//
//        // then
//        assertThat(thrown).isInstanceOf(PaymentException.class);
//    }
//
//    @Test
//    @DisplayName("포인트 결제를 진행한다 - 실패 케이스(중복 결제)")
//    void payByCashOnlyInvalidRequest() {
//        // given
//        guest.addCash(500000L, 0L);
//        System.out.println(guest.getRestCash());
//        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 0L);
//
//        // when
//        payService.payByCashOnly(reservation, useCouponRequest);
//        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));
//
//        // then
//        assertThat(thrown).isInstanceOf(PaymentException.class);
//    }
//
//
//    @Test
//    @DisplayName("복합 혹은 토스페이먼츠 결제를 진행한다")
//    void payByTossPayments() {
//        // given
//        guest.addCash(50000L, 0L);
//        UseCouponRequest useCouponRequest = new UseCouponRequest(신규회원, 10000L);
//        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest("NORMAL", "oa4CWyWY5m89PNh7xJwhk1", "100000", "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6", 0L, 신규회원);
//        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest("o8sJILLP1EP6V1nLksCBL", 100000L, "간편결제", "DONE", "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ", "20", null);
//
////        when(tossService.confirmTossPayment(any())).thenReturn(Mono.just(tossPaymentRequest));
//
//        // when
//        payService.payByTossPayments(tossConfirmRequest, reservation, 0L);
//
//        // then
//        assertThat(reservation.getOrderId()).startsWith("o");
//        assertThat(reservation.isPaid()).isTrue();
//    }
//
//    @Test
//    @DisplayName("복합 혹은 토스페이먼츠 결제를 진행한다 - 실패 케이스(잘못된 요청)")
//    void payByTossPaymentsInvalidRequest() {
//        // given
//        guest.addCash(0L, 0L);
//        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest("NORMAL", "oa4CWyWY5m89PNh7xJwhk1", "50000", "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6", 0L, 신규회원);
//
//        // when
//        Throwable thrown = catchThrowable(() -> payService.payByTossPayments(tossConfirmRequest, reservation, 0L));
//
//        // then
//        assertThat(thrown).isInstanceOf(PaymentException.class);
//    }
//}
