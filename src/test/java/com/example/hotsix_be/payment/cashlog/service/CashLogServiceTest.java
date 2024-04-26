package com.example.hotsix_be.payment.cashlog.service;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.dto.InitCashLogDto;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.ALREADY_BEEN_INITIALIZED;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_AUTHORITY;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.결제__예치금;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashLogServiceTest {
    @InjectMocks
    private CashLogService cashLogService;
    @Mock
    private CashLogRepository cashLogRepository;
    @Mock
    private ReservationService reservationService;
    @Mock
    private MemberService memberService;

    private final Long memberId = 1L;
    private final String guestUsername = "KIM";
    private final Long restCash = 30_000L;

    private final Long price = -10_000L;
    private final String orderId = "o8sJILLP1EP6V1nLksCBL";
    private final Long discountAmount = 0L;
    private final EventType eventType = 결제__예치금;

    private final Accessor accessor = getDataAccessor();

    private final Pageable pageable = PageRequest.of(0, 10);


    @Test
    @DisplayName("새로운 CashLog 를 생성하여 CashLogMarker 에 입력 후 포인트를 이동")
    void addCashLog() {
        // given
        Member member = Member.builder().restCash(restCash).build();
        CashLogMarker cashLogMarker = Pay.builder().build();

        // when
        cashLogService.addCashLog(InitCashLogDto.of(
                        member,
                        price,
                        orderId,
                        eventType,
                        cashLogMarker
                ),
                discountAmount);

        // then
        assertThat(member.getRestCash()).isEqualTo(restCash + price);
        assertThat(cashLogMarker.isPaid()).isTrue();
        assertThat(cashLogMarker.isInitialized()).isTrue();
    }

    @Test
    @DisplayName("CashLogMarker 에 initCashLog 를 통한 초기화를 두번 진행할 경우 오류를 발생시킨다.")
    void initCashLogAlreadyBeenInitialized() {
        // given
        Member member = Member.builder().restCash(restCash).build();
        CashLogMarker cashLogMarker = Pay.builder().build();

        // when
        cashLogService.initCashLog(
                InitCashLogDto.of(
                        member,
                        price,
                        "o8sJILLP1EP6V1nLksCBL",
                        결제__예치금,
                        cashLogMarker)
        );

        Throwable thrown = catchThrowable(() ->
                cashLogService.initCashLog(
                        InitCashLogDto.of(
                        member,
                        price,
                        "o8sJILLP1EP6V1nLksCBL",
                        결제__예치금,
                        cashLogMarker)
                ));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(ALREADY_BEEN_INITIALIZED.getMessage());
    }

    @Test
    @DisplayName("나의 CashLog 들을 MyCashLogResponse 형태로 반환한다.")
    void findMyPageList() {
        // given
        Member member = Member.builder().username(guestUsername).build();
        List<CashLogConfirmResponse> cashLogConfirmResponses = new ArrayList<>();

        when(memberService.getMemberById(any())).thenReturn(member);
        when(cashLogRepository.getCashLogConfirmResForPayByMember(any(Member.class), any(Pageable.class))).thenReturn(new PageImpl<>(cashLogConfirmResponses, pageable, 0));

        // when
        MyCashLogResponse result = cashLogService.findMyPageList(accessor, pageable);

        // then
        assertThat(result.getCashLogConfirmPage()).isNotNull();
        assertThat(result.getCashLogConfirmPage().getTotalElements()).isEqualTo(0L);
        assertThat(result.getUsername())
                .isEqualTo("KIM");
    }

    @Test
    @DisplayName("예약 결제 확인을 위해 ConfirmResponse 를 반환한다.")
    void getConfirmRespById() {
        // given
        Accessor accessor = Accessor.member(1L);
        Member member = Member.builder().id(1L).build();
        Long price = 10_000L;
        String nickName = "HotShare";
        Optional<CashLog> cashLog = Optional.ofNullable(CashLog.builder().id(1L).amount(price).member(member).eventType(결제__예치금).build());
        Member wrongMember = Member.builder().id(2L).build();
        Optional<CashLog> wrongCashLog = Optional.ofNullable(CashLog.builder().id(2L).amount(price).member(wrongMember).eventType(결제__예치금).build());
        Hotel hotel = Hotel.builder().nickname(nickName).build();
        Reservation reservation = Reservation.builder()
                .id(1L)
                .member(member)
                .hotel(hotel)
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now())
                .build();

        when(cashLogRepository.findById(1L)).thenReturn(cashLog);
        when(cashLogRepository.findById(2L)).thenReturn(wrongCashLog);
        when(reservationService.findByOrderIdAndMember(any(), any())).thenReturn(reservation);

        // when
        ConfirmResponse result = cashLogService.getConfirmRespById(1L, accessor);

        Throwable thrown = catchThrowable(() -> cashLogService.getConfirmRespById(2L, accessor));

        // then
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getHotelNickname()).isEqualTo(nickName);

        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("생성된 CashLog 의 Id 값만을 취하는 CashLogIdResponse 를 반환한다.")
    void getCashLogIdById() {
        // given
        Long id = 1L;

        // when
        CashLogIdResponse result = cashLogService.getCashLogIdById(id);

        // then
        assertThat(result.getCashLogId()).isEqualTo(id);
    }

    private Member getMember() {
        return Member.builder()
                .username("KIM")
                .build();
    }

    private Accessor getDataAccessor() {
        return Accessor.member(1L);
    }
}
