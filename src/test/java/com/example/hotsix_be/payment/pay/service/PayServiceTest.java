package com.example.hotsix_be.payment.pay.service;

import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.dto.InitCashLogDto;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.example.hotsix_be.payment.pay.repository.PayRepository;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import com.example.hotsix_be.reservation.entity.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.INSUFFICIENT_DEPOSIT;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.coupon.entity.CouponType.신규회원;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.결제__예치금;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.결제__토스페이먼츠;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    private final Long hostId = 2L;
    private final String hostNickname = "Kim";

    private final Long guestId = 1L;
    private final String guestNickname = "Park";
    private final Long guestRestCash = 20_000L;

    private final Long wrongGuestRestCash = 0L;

    private final String orderId = "o8sJILLP1EP6V1nLksCBL";
    private final Long totalAmount = 50_000L;
    private final String method = "간편결제";
    private final String status = "DONE";
    private final String secret = "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ";
    private final String bankCode = "20";

    private final String paymentType = "NORMAL";
    private final String pgPrice = "100000";
    private final String paymentKey = "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6";

    private final String wrongPgPrice = "0";

    private final CouponType couponType = 신규회원;
    private final Long discountAmount = 0L;

    private final String hotelType = "호텔";
    private final String address = "서울시 강남구";
    private final String addressDetail = "강남역 1번출구";
    private final Long roomCnt = 10L;
    private final Long bedCnt = 10L;
    private final Long bathroomCnt = 10L;
    private final Long maxPeople = 10L;
    private final List<String> facility = List.of("헬스장");
    private final String hotelNickname = "강남호텔";
    private final String description = "강남호텔입니다.";
    private final Long price = 10_000L;

    private final LocalDate checkInDate = null;
    private final LocalDate checkOutDate = null;
    private final Long guests = 2L;
    private final Long reservePrice = 10_000L;
    private final boolean isPaid = false;

    private final EventType pointPayEventType = 결제__예치금;
    private final EventType tossPayEventType = 결제__토스페이먼츠;

    private final Hotel hotel = getDataHotel();

    @Test
    @DisplayName("포인트 결제 진행")
    void payByCashOnly() {
        // given
        Member guest = Member.builder()
                .id(guestId)
                .nickname(guestNickname)
                .restCash(guestRestCash)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(guests)
                .price(reservePrice)
                .isPaid(isPaid)
                .hotel(hotel)
                .member(guest)
                .build();

        UseCouponRequest useCouponRequest = new UseCouponRequest(couponType, discountAmount);

        ArgumentCaptor<InitCashLogDto> initCashLogDtoCaptor = ArgumentCaptor.forClass(InitCashLogDto.class);
        ArgumentCaptor<Pay> payCaptor = ArgumentCaptor.forClass(Pay.class);

        // when
        payService.payByCashOnly(reservation, useCouponRequest);

        // then
        verify(cashLogService).addCashLog(initCashLogDtoCaptor.capture(), any(Long.class));
        verify(payRepository).save(payCaptor.capture());

        InitCashLogDto savedInitCashLogDto = initCashLogDtoCaptor.getValue();
        Pay savedPay = payCaptor.getValue();

        assertThat(reservation.getOrderId()).startsWith("o");
        assertThat(reservation.isPaid()).isTrue();

        assertThat(savedInitCashLogDto.getMember().getId()).isEqualTo(guest.getId());
        assertThat(savedInitCashLogDto.getPrice()).isEqualTo(reservation.getPrice() * -1);
        assertThat(savedInitCashLogDto.getOrderId()).isEqualTo(reservation.getOrderId());
        assertThat(savedInitCashLogDto.getEventType()).isEqualTo(pointPayEventType);
        assertThat(savedInitCashLogDto.getCashLogMarker()).isInstanceOf(Pay.class);

        assertThat(savedPay.getReservation()).isEqualTo(reservation);
    }

    @Test
    @DisplayName("포인트 결제 진행 실패  - 잔액 부족")
    void payByCashOnlyInsufficientDeposit() {
        // given
        Member guest = Member.builder()
                .id(guestId)
                .nickname(guestNickname)
                .restCash(wrongGuestRestCash)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(guests)
                .price(reservePrice)
                .isPaid(isPaid)
                .hotel(hotel)
                .member(guest)
                .build();

        UseCouponRequest useCouponRequest = new UseCouponRequest(couponType, discountAmount);

        // when
        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));

        // then
        assertThat(thrown)
                .isInstanceOf(PaymentException.class)
                .hasMessage(INSUFFICIENT_DEPOSIT.getMessage());
    }

    @Test
    @DisplayName("포인트 결제 진행 - 중복 결제")
    void payByCashOnlyInvalidRequest() {
        // given
        Member guest = Member.builder()
                .id(guestId)
                .nickname(guestNickname)
                .restCash(guestRestCash)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(guests)
                .price(reservePrice)
                .isPaid(isPaid)
                .hotel(hotel)
                .member(guest)
                .build();

        UseCouponRequest useCouponRequest = new UseCouponRequest(couponType, discountAmount);

        // when
        payService.payByCashOnly(reservation, useCouponRequest);
        Throwable thrown = catchThrowable(() -> payService.payByCashOnly(reservation, useCouponRequest));

        // then
        assertThat(thrown)
                .isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_REQUEST.getMessage());
    }


    @Test
    @DisplayName("복합 혹은 토스페이먼츠 결제 진행")
    void payByTossPayments() {
        // given
        Member guest = Member.builder()
                .id(guestId)
                .nickname(guestNickname)
                .restCash(guestRestCash)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(guests)
                .price(reservePrice)
                .isPaid(isPaid)
                .hotel(hotel)
                .member(guest)
                .build();

        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest(
                paymentType,
                orderId,
                pgPrice,
                paymentKey,
                discountAmount,
                couponType
        );

        when(tossService.confirmTossPayment(any())).thenReturn(this.getTossPaymentRequestMono());

        ArgumentCaptor<InitCashLogDto> initCashLogDtoCaptor = ArgumentCaptor.forClass(InitCashLogDto.class);
        ArgumentCaptor<Pay> payCaptor = ArgumentCaptor.forClass(Pay.class);

        // when
        payService.payByTossPayments(tossConfirmRequest, reservation, discountAmount);

        // then
        verify(cashLogService).addCashLog(initCashLogDtoCaptor.capture(), any(Long.class));
        verify(payRepository).save(payCaptor.capture());

        InitCashLogDto savedInitCashLogDto = initCashLogDtoCaptor.getValue();
        Pay savedPay = payCaptor.getValue();

        assertThat(reservation.getOrderId()).startsWith("o");
        assertThat(reservation.isPaid()).isTrue();

        assertThat(savedInitCashLogDto.getMember().getId()).isEqualTo(guest.getId());
        assertThat(savedInitCashLogDto.getPrice()).isEqualTo(reservation.getPrice() * -1);
        assertThat(savedInitCashLogDto.getOrderId()).isEqualTo(reservation.getOrderId());
        assertThat(savedInitCashLogDto.getEventType()).isEqualTo(tossPayEventType);
        assertThat(savedInitCashLogDto.getCashLogMarker()).isInstanceOf(Pay.class);

        assertThat(savedPay.getReservation()).isEqualTo(reservation);
    }

    @Test
    @DisplayName("복합 혹은 토스페이먼츠 결제 진행 - 잘못된 요청")
    void payByTossPaymentsInvalidRequest() {
        // given
        Member guest = Member.builder()
                .id(guestId)
                .nickname(guestNickname)
                .restCash(wrongGuestRestCash)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(guests)
                .price(reservePrice)
                .isPaid(isPaid)
                .hotel(hotel)
                .member(guest)
                .build();

        TossConfirmRequest tossConfirmRequest = new TossConfirmRequest(
                paymentType,
                orderId,
                wrongPgPrice,
                paymentKey,
                discountAmount,
                couponType
        );

        // when
        Throwable thrown = catchThrowable(() -> payService.payByTossPayments(tossConfirmRequest, reservation, 0L));

        // then
        assertThat(thrown)
                .isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_REQUEST.getMessage());
    }

    private Mono<TossPaymentRequest> getTossPaymentRequestMono() {
        return Mono.just(new TossPaymentRequest(
                orderId,
                totalAmount,
                method,
                status,
                secret,
                bankCode,
                null
        ));
    }

    private Hotel getDataHotel() {
        return Hotel.builder()
                .hotelType(hotelType)
                .address(address)
                .addressDetail(addressDetail)
                .roomCnt(roomCnt)
                .bedCnt(bedCnt)
                .bathroomCnt(bathroomCnt)
                .maxPeople(maxPeople)
                .facility(facility)
                .nickname(hotelNickname)
                .description(description)
                .price(price)
                .owner(this.getHost())
                .build();
    }

    private Member getHost() {
        return Member.builder()
                .id(hostId)
                .nickname(hostNickname)
                .build();
    }
}
