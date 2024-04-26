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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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

    private final Long guestId = 1L;
    private final String guestUsername = "KIM";
    private final Long restCash = 30_000L;

    private final Long hostId = 2L;
    private final String hostUsername = "Lee";

    private final Long wrongMemberId = 3L;
    private final String wrongMemberUsername = "Park";

    private final Long cashLogId = 1L;
    private final Long price = -10_000L;
    private final Long discountAmount = 0L;
    private final EventType eventType = 결제__예치금;
    private final LocalDateTime createdAt = null;

    private final Long wrongCashLogId = 2L;

    private final Long reserveId = 1L;
    private final String orderId = "o8sJILLP1EP6V1nLksCBL";

    private final Long hotelId = 1L;
    private final String hotelNickname = "HotShare";

    private final Accessor accessor = getDataAccessor();

    private final Pageable pageable = PageRequest.of(0, 10);


    @Test
    @DisplayName("새로운 CashLog 를 생성하여 CashLogMarker 에 입력 후 포인트를 이동")
    void addCashLog() {
        // given
        Member member = Member.builder().restCash(restCash).build();
        CashLogMarker cashLogMarker = Pay.builder().build();

        // when
        cashLogService.addCashLog(
                InitCashLogDto.of(
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
    @DisplayName("CashLogMarker 에 initCashLog 를 통한 초기화를 두번 진행할 경우 오류 반환 - 이미 초기화된 CashLog")
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
        assertThat(thrown)
                .isInstanceOf(PaymentException.class)
                .hasMessage(ALREADY_BEEN_INITIALIZED.getMessage());
    }

    @Test
    @DisplayName("나의 CashLog 들을 MyCashLogResponse 형태로 반환")
    void findMyPageList() {
        // given
        when(memberService.getMemberById(any())).thenReturn(this.getGuest());
        when(cashLogRepository.getCashLogConfirmResForPayByMember(any(Member.class), any(Pageable.class))).thenReturn(this.getCashLogConfirmResponses());

        // when
        MyCashLogResponse result = cashLogService.findMyPageList(accessor, pageable);

        // then
        assertThat(result.getCashLogConfirmPage()).isNotNull();
        assertThat(result.getUsername()).isEqualTo(guestUsername);
    }

    @Test
    @DisplayName("예약 결제 확인을 위해 ConfirmResponse 를 반환")
    void getConfirmRespById() {
        // given
        when(cashLogRepository.findById(cashLogId)).thenReturn(this.getCashLogOne());
        when(reservationService.findByOrderIdAndMember(any(String.class), any(Member.class))).thenReturn(this.getReserve());

        // when
        ConfirmResponse result = cashLogService.getConfirmRespById(cashLogId, accessor);

        // then
        assertThat(result.getPrice()).isEqualTo(-price);
        assertThat(result.getHotelNickname()).isEqualTo(hotelNickname);
    }

    @Test
    @DisplayName("예약 결제 확인을 위해 ConfirmResponse 를 반환 실패 - 다른 계정의 CashLog 조회 시도")
    void getConfirmRespByIdInvalidAuthority() {
        // given
        when(cashLogRepository.findById(wrongCashLogId)).thenReturn(this.getWrongCashLogOne());

        // when
        Throwable thrown = catchThrowable(() -> cashLogService.getConfirmRespById(wrongCashLogId, accessor));

        // then
        assertThat(thrown)
                .isInstanceOf(PaymentException.class)
                .hasMessage(INVALID_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("생성된 CashLog 의 Id 값만을 취하는 CashLogIdResponse 를 반환")
    void getCashLogIdById() {
        // when
        CashLogIdResponse result = cashLogService.getCashLogIdById(cashLogId);

        // then
        assertThat(result.getCashLogId()).isEqualTo(cashLogId);
    }

    private Member getGuest() {
        return Member.builder()
                .id(guestId)
                .username(guestUsername)
                .build();
    }

    private Member getHost() {
        return Member.builder()
                .id(hostId)
                .username(hostUsername)
                .build();
    }

    private Member getWrongMember() {
        return Member.builder()
                .id(wrongMemberId)
                .username(wrongMemberUsername)
                .build();
    }

    private Accessor getDataAccessor() {
        return Accessor.member(guestId);
    }

    private Optional<CashLog> getCashLogOne() {
        return Optional.ofNullable(this.getCashLog());
    }

    private Optional<CashLog> getWrongCashLogOne() {
        return Optional.ofNullable(this.getWrongCashLog());
    }

    private CashLog getCashLog() {
        return CashLog.builder()
                .id(cashLogId)
                .amount(price)
                .member(this.getGuest())
                .eventType(eventType)
                .orderId(orderId)
                .build();
    }

    private CashLog getWrongCashLog() {
        return CashLog.builder()
                .id(wrongCashLogId)
                .amount(price)
                .member(this.getWrongMember())
                .eventType(결제__예치금)
                .build();
    }

    private Reservation getReserve() {
        return Reservation.builder()
                .id(reserveId)
                .orderId(orderId)
                .member(this.getGuest())
                .host(this.getHost())
                .hotel(this.getHotel())
                .build();
    }

    private Hotel getHotel() {
        return Hotel.builder()
                .id(hotelId)
                .nickname(hotelNickname)
                .build();
    }

    private Page<CashLogConfirmResponse> getCashLogConfirmResponses() {
        List<CashLogConfirmResponse> list = List.of(this.getCashLogConfirmResponseData());
        return new PageImpl<>(list, pageable, list.size());
    }

    private CashLogConfirmResponse getCashLogConfirmResponseData() {
        return new CashLogConfirmResponse(
                cashLogId,
                eventType,
                price,
                guestId,
                orderId,
                createdAt
        );
    }
}
